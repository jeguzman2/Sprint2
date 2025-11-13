package com.provesi.demo.web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

public class DemoController {

    @GetMapping("/")
  public String home() {
    return "Home público. Ve a /me (login) y /admin/ping (solo ADMIN).";
  }

  @GetMapping("/me")
  public Map<String, Object> me(@AuthenticationPrincipal OidcUser user) {
    System.out.println(user);
    // Construimos un mapa que soporte valores null sin romper
    Map<String, Object> out = new LinkedHashMap<>();
    out.put("sub", user.getSubject());

    if (user.getEmail() != null) {
      out.put("email", user.getEmail());
    }

    // Muestra TODO el set de claims para depurar
    out.put("all_claims", user.getClaims());

    // Authorities legibles
    List<String> authorities = user.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
    out.put("authorities", authorities);

    // Si existe el namespace, lo añadimos aparte para verlo fácil
    Object ns = user.getClaims().get("https://provesi.com/claims");
    if (ns != null) {
      out.put("claims_ns", ns);
    }

    return out;
  }
}
