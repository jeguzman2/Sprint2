package com.provesi.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import java.util.*;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize
public class SecurityConfig {

  @Value("${provesi.auth.role-namespace}")
  private String roleNamespace;

  @Bean
  SecurityFilterChain filter(HttpSecurity http, ClientRegistrationRepository clients) throws Exception {
    http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/health", "/public/**", "/").permitAll()
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .anyRequest().authenticated()
    )
    .oauth2Login(oauth -> oauth
      .loginPage("/oauth2/authorization/auth0")
      // AQUI: pásale un OAuth2UserService, no un usuario
      .userInfoEndpoint(u -> u.oidcUserService(oidcUserService()))
    )
    .logout(logout -> logout.logoutSuccessHandler(oidcLogout(clients)));

  return http.build();
  }

/** Devuelve un OAuth2UserService<OidcUserRequest, OidcUser> */
private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
  OidcUserService delegate = new OidcUserService();
  return (OidcUserRequest req) -> {
    OidcUser user = delegate.loadUser(req);

    // ----- Mapeo de rol desde tu namespace -----
    String NS = roleNamespace; // inyectado con @Value
    Object nsObj = user.getClaims().get(NS);
    String role = null;
    if (nsObj instanceof Map<?,?> map) {
      Object r = map.get("role");
      role = (r == null) ? null : r.toString();
    }

    // Authorities originales + ROLE_XXX si viene en el claim
    var authorities = new java.util.HashSet<GrantedAuthority>(user.getAuthorities());
    if (role != null && !role.isBlank()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
    }

    // devolvemos un OidcUser con esas authorities
    return new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo());
  };
}


  private LogoutSuccessHandler oidcLogout(ClientRegistrationRepository clients) {
    var h = new OidcClientInitiatedLogoutSuccessHandler(clients);
    // tu URL pública/home
    h.setPostLogoutRedirectUri("http://3.95.118.101:8080/");
    return h;
  }
}
