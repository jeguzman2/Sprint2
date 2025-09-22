package com.provesi.demo.controller;

import com.provesi.demo.DetailDTO.ProductoDetalleDTO;
import com.provesi.demo.model.Producto;
import com.provesi.demo.service.ConsultaProductoService;
import com.provesi.demo.service.ProductoService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ConsultaProductoService consultaProductoService;

  public ProductoController(ProductoService productoService, ConsultaProductoService consultaProductoService) {
    this.productoService = productoService;
    this.consultaProductoService = consultaProductoService;
  }

  @PostMapping
  public ResponseEntity<Producto> crear(@RequestBody Producto p) {
    Producto creado = productoService.crear(p);
    return ResponseEntity.ok(creado);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto cambios) {
    Producto actualizado = productoService.actualizar(id, cambios);
    return ResponseEntity.ok(actualizado);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
    Producto p = productoService.obtenerPorId(id);
    return ResponseEntity.ok(p);
  }

  @GetMapping("/productos/{id}")
  public ProductoDetalleDTO consultarProducto(@PathVariable Long id) {
        return consultaProductoService.consultarProductoPorId(id);
    }

  @GetMapping
  public ResponseEntity<List<Producto>> listar() {
    List<Producto> lista = productoService.listar();
    return ResponseEntity.ok(lista);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    productoService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
