package com.provesi.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Claim personalizado donde pones roles en Auth0 (ajústalo si usas otro)
    private static final String ROLES_CLAIM = "https://provesi.com/roles";

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${auth0.audience}")
    private String audience;

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/productos/").permitAll()
                .requestMatchers("/api/admin/**").hasRole("admin")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(this::jwtAuthConverter)
                )
            );

        return http.build();
    }

    // JwtDecoder requerido (soluciona el error)
    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> withIssuer   = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> validator    =
                new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience);

        decoder.setJwtValidator(validator);
        return decoder;
    }

    // Convierte roles del claim personalizado a autoridades de Spring
    private AbstractAuthenticationToken jwtAuthConverter(Jwt jwt) {
        // Si además usas "scope" / "permissions", puedes combinarlo:
        JwtGrantedAuthoritiesConverter scopeConv = new JwtGrantedAuthoritiesConverter();
        var fromScopes = scopeConv.convert(jwt);

        var fromCustomRoles = ((List<String>) jwt.getClaims()
                .getOrDefault(ROLES_CLAIM, List.of()))
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();

        var authorities = new java.util.ArrayList<>(fromScopes);
        authorities.addAll(fromCustomRoles);

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
