package com.provesi.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.provesi.demo.model.Usuario;
import com.provesi.demo.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  @PostMapping
  public ResponseEntity<Usuario> crear(@RequestBody Usuario u) {
    Usuario creado = usuarioService.crear(u);
    return ResponseEntity.ok(creado);
  }

  @PutMapping("/{id}")
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

  @GetMapping
  public ResponseEntity<List<Usuario>> listar() {
    List<Usuario> lista = usuarioService.listar();
    return ResponseEntity.ok(lista);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    usuarioService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
