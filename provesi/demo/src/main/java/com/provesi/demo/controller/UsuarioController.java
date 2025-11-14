package com.provesi.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.GrantedAuthority;

import com.provesi.demo.model.Usuario;
import com.provesi.demo.service.UsuarioService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PersistenceContext
    private EntityManager entityManager;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping
  public ResponseEntity<Usuario> crear(@RequestBody Usuario u) {
    Usuario creado = usuarioService.crear(u);
    return ResponseEntity.ok(creado);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario cambios) {
    Usuario actualizado = usuarioService.actualizar(id, cambios);
    return ResponseEntity.ok(actualizado);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
    Usuario u = usuarioService.obtener(id);
    return ResponseEntity.ok(u);
  }

  @GetMapping("/email")
  public ResponseEntity<Usuario> obtenerPorEmail(@RequestParam String email) {
    Usuario u = usuarioService.obtenerPorEmail(email);
    return ResponseEntity.ok(u);
  }

@GetMapping("")
@PreAuthorize("isAuthenticated()")  // opcional, pero recomendado
public ResponseEntity<List<Usuario>> listarUsuarios(
        @AuthenticationPrincipal OidcUser user) {

    // Authorities: aquí ya deben venir ROLE_ADMIN, ROLE_SUPERVISOR, etc.
    var authorities = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

    System.out.println("Authorities = " + authorities);

    // Si quieres el rol que metiste en el claim "claims":
    Object claimsObj = user.getClaims().get("claims");
    String rolDesdeClaim = null;
    if (claimsObj instanceof Map<?,?> map) {
        Object r = map.get("role");
        if (r != null) rolDesdeClaim = r.toString();
    }
    System.out.println("Rol desde claim = " + rolDesdeClaim);

    if (rolDesdeClaim != null && rolDesdeClaim.equals("ADMIN")) {
        System.out.println("Acceso concedido para listar usuarios.");
    } else {
      System.out.println("Acceso denegado para listar usuarios.");
        return ResponseEntity.status(401).build();
    }
  
    List<Usuario> usuarios = usuarioService.listar();
    return ResponseEntity.ok(usuarios);
}

@GetMapping("/vulnerable")
public ResponseEntity<?> probarVulnerable(@RequestParam String idRaw) {
    Map<String, Object> body = new HashMap<>();

    try {
        // 1. Detectar ataque antes de tocar la BD
        String lower = idRaw.toLowerCase();
        boolean esMalicioso =
                lower.contains(" or ")
             || lower.contains(" and ")
             || lower.contains("1=1")
             || lower.contains("--")
             || lower.contains("/*")
             || lower.contains("*/")
             || lower.contains(" drop ")
             || lower.contains(" delete ")
             || lower.contains(" update ")
             || lower.contains(" insert ")
             || lower.contains(" union ");

        if (esMalicioso) {
            body.put("msg", "Entrada maliciosa detectada en el parámetro id.");
            body.put("status", 400);
            return ResponseEntity.badRequest().body(body);
        }

        // 2. Si no es malicioso, ejecutamos la query VULNERABLE
        String sql = "SELECT * FROM usuarios WHERE id_usuario = " + idRaw;

        List<Usuario> usuarios = entityManager
                .createNativeQuery(sql, Usuario.class)
                .getResultList();

        return ResponseEntity.ok(usuarios);

    } catch (Exception e) {
        // 3. Cualquier excepción inesperada se responde como JSON
        body.put("msg", "Error interno del servidor: " + e.getClass().getSimpleName());
        body.put("status", 500);
        return ResponseEntity.internalServerError().body(body);
    }
}


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    usuarioService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
