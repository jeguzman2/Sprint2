package com.provesi.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.provesi.demo.DTO.InventarioDTO;
import com.provesi.demo.model.EstadoInventario;
import com.provesi.demo.model.Inventario;
import com.provesi.demo.model.Producto;
import com.provesi.demo.model.Ubicacion;
import com.provesi.demo.service.InventarioService;
import com.provesi.demo.repositorios.InventarioRepository;
import com.provesi.demo.repositorios.ProductoRepository;
import com.provesi.demo.repositorios.UbicacionRepository;


@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    private final ProductoRepository productoRepo;
    private final UbicacionRepository ubicacionRepo;
    private final InventarioRepository inventarioRepo;

  public InventarioController(InventarioService inventarioService, ProductoRepository productoRepo, UbicacionRepository ubicacionRepo, InventarioRepository inventarioRepo) {
    this.inventarioService = inventarioService;
    this.productoRepo = productoRepo;
    this.ubicacionRepo = ubicacionRepo;
    this.inventarioRepo = inventarioRepo;
  }

  // Crear item de inventario vinculando producto y ubicación
  @PostMapping
  public Inventario crearItem(@RequestBody InventarioDTO request) {
    Producto producto = productoRepo.findById(request.getProductoId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + request.getProductoId()));

    Ubicacion ubicacion = ubicacionRepo.findById(request.getIdUbicacion())
            .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada: " + request.getIdUbicacion()));

    Inventario inv = new Inventario();
    inv.setProducto(producto);
    inv.setUbicacion(ubicacion);
    inv.setCantidadDisponible(request.getCantidadDisponible());
    inv.setEstado(request.getEstado());

    return inventarioRepo.save(inv);
}

  // Ajustar cantidad (delta puede ser negativo o positivo)
  @PatchMapping("/{id}/ajustar")
  public ResponseEntity<Inventario> ajustarCantidad(@PathVariable Long id, @RequestParam int delta) {
    Inventario item = inventarioService.ajustarCantidad(id, delta);
    return ResponseEntity.ok(item);
  }

  // Cambiar estado (DISPONIBLE, RESERVADO, DAÑADO, AGOTADO)
  @PatchMapping("/{id}/estado")
  public ResponseEntity<Inventario> cambiarEstado(@PathVariable Long id, @RequestParam EstadoInventario estado) {
    Inventario item = inventarioService.cambiarEstado(id, estado);
    return ResponseEntity.ok(item);
  }

  // Mover de ubicación
  @PatchMapping("/{id}/mover")
  public ResponseEntity<Inventario> mover(@PathVariable Long id, @RequestParam Long nuevaUbicacionId) {
    Inventario item = inventarioService.mover(id, nuevaUbicacionId);
    return ResponseEntity.ok(item);
  }

  // Listar por código de producto
  @GetMapping("/por-producto/{codigo}")
  public ResponseEntity<List<Inventario>> listarPorCodigoProducto(@PathVariable String codigo) {
    List<Inventario> lista = inventarioService.listarPorCodigoProducto(codigo);
    return ResponseEntity.ok(lista);
  }

  @GetMapping
  public ResponseEntity<List<Inventario>> listarTodos() {
    List<Inventario> lista = inventarioService.listarTodos();
    return ResponseEntity.ok(lista);
  }
  

  // Eliminar item
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    inventarioService.eliminar(id);
    return ResponseEntity.noContent().build();
  }
}
