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
import org.springframework.web.bind.annotation.RestController;

import com.provesi.demo.model.Bodega;
import com.provesi.demo.service.BodegaService;

@RestController
@RequestMapping("/bodegas")
public class BodegaController {
    private final BodegaService bodegaService;

  public BodegaController(BodegaService bodegaService) {
    this.bodegaService = bodegaService;
  }

  @PostMapping
  public ResponseEntity<Bodega> crear(@RequestBody Bodega b) {
    Bodega creada = bodegaService.crear(b);
    return ResponseEntity.ok(creada);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Bodega> actualizar(@PathVariable Long id, @RequestBody Bodega cambios) {
    Bodega actualizada = bodegaService.actualizar(id, cambios);
    return ResponseEntity.ok(actualizada);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Bodega> obtener(@PathVariable Long id) {
    Bodega b = bodegaService.obtener(id);
    return ResponseEntity.ok(b);
  }

  @GetMapping
  public ResponseEntity<List<Bodega>> listar() {
    List<Bodega> lista = bodegaService.listar();
    return ResponseEntity.ok(lista);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    bodegaService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
