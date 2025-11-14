package com.provesi.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;

import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${provesi.auth.role-namespace}")
    private String roleNamespace;

    @Bean
    SecurityFilterChain filter(HttpSecurity http, ClientRegistrationRepository clients, TestAuthFilter testAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(testAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health", "/public/**", "/").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .loginPage("/oauth2/authorization/auth0")
                .userInfoEndpoint(u -> u.oidcUserService(oidcUserService()))
            )
            .logout(logout -> logout.logoutSuccessHandler(oidcLogout(clients)));

        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();

        return (OidcUserRequest req) -> {
            OidcUser user = delegate.loadUser(req);

            // Mapeo del rol desde tu namespace
            String NS = roleNamespace;
            Object nsObj = user.getClaims().get(NS);

            String role = null;

            if (nsObj instanceof Map<?, ?> map) {
                Object r = map.get("role");
                role = (r == null) ? null : r.toString();
            }

            // Authorities originales + ROLE_XXX si viene en el claim
            var authorities = new HashSet<GrantedAuthority>(user.getAuthorities());

            if (role != null && !role.isBlank()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }

            return new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo());
        };
    }

    private LogoutSuccessHandler oidcLogout(ClientRegistrationRepository clients) {
        var handler = new OidcClientInitiatedLogoutSuccessHandler(clients);

        handler.setPostLogoutRedirectUri("http://3.95.118.101:8080/");
        return handler;
    }
}
