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

import com.provesi.demo.model.Ubicacion;
import com.provesi.demo.service.UbicacionService;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

  public UbicacionController(UbicacionService ubicacionService) {
    this.ubicacionService = ubicacionService;
  }

  // Crear indicando la bodega destino vía query param
  @PostMapping
  public ResponseEntity<Ubicacion> crear(@RequestBody Ubicacion u, @RequestParam Long idBodega) {
    Ubicacion creada = ubicacionService.crear(u, idBodega);
    return ResponseEntity.ok(creada);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Ubicacion> actualizar(@PathVariable Long id,
                                              @RequestBody Ubicacion cambios,
                                              @RequestParam(required = false) Long idBodega) {
    Ubicacion actualizada = ubicacionService.actualizar(id, cambios, idBodega);
    return ResponseEntity.ok(actualizada);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Ubicacion> obtener(@PathVariable Long id) {
    Ubicacion u = ubicacionService.obtener(id);
    return ResponseEntity.ok(u);
  }

  @GetMapping
  public ResponseEntity<List<Ubicacion>> listar() {
    List<Ubicacion> lista = ubicacionService.listar();
    return ResponseEntity.ok(lista);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    ubicacionService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
