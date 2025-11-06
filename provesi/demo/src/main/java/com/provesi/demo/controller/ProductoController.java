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

import com.provesi.demo.DetailDTO.ProductoDetalleDTO;
import com.provesi.demo.model.EstadoProducto;
import com.provesi.demo.model.Producto;
import com.provesi.demo.service.ConsultaProductoService;
import com.provesi.demo.service.ProductoService;

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

  //ASR2- ACTUALIZAR EL ESTADO DE UN PRODUCTO
  @PutMapping("/{id}/estado")
    public ResponseEntity<Producto> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoProducto nuevoEstado) {
        Producto productoActualizado = productoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(productoActualizado);
    }


  @GetMapping("/{id}")
  public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
    Producto p = productoService.obtenerPorId(id);
    return ResponseEntity.ok(p);
  }
   //Prueba del ASR1- LATENCIA 
  @GetMapping("/productos/{id}")
  public ProductoDetalleDTO consultarProducto(@PathVariable Long id) {
        return consultaProductoService.consultarProductoPorId(id);
    }

    //ASR 1- LATENCIA
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
