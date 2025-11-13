package com.provesi.demo.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class DemoController {

    @GetMapping("/")
  public String home() {
    return "Home público. Ve a /me (login) y /admin/ping (solo ADMIN).";
  }

  @GetMapping("/me")
  public Map<String, Object> me(@AuthenticationPrincipal OidcUser user) {
    System.out.println(user);
    return Map.of(
      "sub", user.getSubject(),
      "email", user.getEmail(),
      "claims_ns", user.getClaims().get("https://provesi.com/claims"),
      "authorities", user.getAuthorities().toString()
    );
  }
}
