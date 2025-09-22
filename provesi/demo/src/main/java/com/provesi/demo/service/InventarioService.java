package com.provesi.demo.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.provesi.demo.model.EstadoInventario;
import com.provesi.demo.model.Inventario;
import com.provesi.demo.model.Producto;
import com.provesi.demo.repositorios.InventarioRepository;
import com.provesi.demo.repositorios.ProductoRepository;
import com.provesi.demo.repositorios.UbicacionRepository;
import com.provesi.demo.model.Ubicacion;

import java.util.List;

@Service
public class InventarioService {

  private final InventarioRepository inventarioRepo;
  private final ProductoRepository productoRepo;
  private final UbicacionRepository ubicacionRepo;

  public InventarioService(InventarioRepository inventarioRepo,
                           ProductoRepository productoRepo,
                           UbicacionRepository ubicacionRepo) {
    this.inventarioRepo = inventarioRepo;
    this.productoRepo = productoRepo;
    this.ubicacionRepo = ubicacionRepo;
  }

  @Transactional
  public Inventario registrarItem(String codigoProducto, Long idUbicacion,
                                      Integer cantidadDisponible, EstadoInventario estado) {
    Producto producto = productoRepo.findByNombre(codigoProducto)
        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + codigoProducto));
    Ubicacion ubicacion = ubicacionRepo.findById(idUbicacion)
        .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada: " + idUbicacion));

    Inventario item = new Inventario();
    item.setProducto(producto);
    item.setUbicacion(ubicacion);
    item.setCantidadDisponible(cantidadDisponible);
    item.setEstado(estado);

    return inventarioRepo.save(item);
  }

  @Transactional
  public Inventario ajustarCantidad(Long idItem, int delta) {
    Inventario item = inventarioRepo.findById(idItem)
        .orElseThrow(() -> new IllegalArgumentException("InventarioItem no encontrado: " + idItem));

    int nueva = item.getCantidadDisponible() + delta;
    if (nueva < 0) throw new IllegalArgumentException("Cantidad no puede ser negativa");
    item.setCantidadDisponible(nueva);
    return inventarioRepo.save(item);
  }

  @Transactional
  public Inventario cambiarEstado(Long idItem, EstadoInventario nuevoEstado) {
    Inventario item = inventarioRepo.findById(idItem)
        .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + idItem));
    item.setEstado(nuevoEstado);
    return inventarioRepo.save(item);
  }

  @Transactional
  public Inventario mover(Long idItem, Long nuevaUbicacionId) {
    Inventario item = inventarioRepo.findById(idItem)
        .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + idItem));
    Ubicacion nuevaUbicacion = ubicacionRepo.findById(nuevaUbicacionId)
        .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada: " + nuevaUbicacionId));
    item.setUbicacion(nuevaUbicacion);
    return inventarioRepo.save(item);
  }

  @Transactional(readOnly = true)
  public List<Inventario> listarPorCodigoProducto(String codigoProducto) {
    long codigoProductoId;
    try {
      codigoProductoId = Long.parseLong(codigoProducto);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El código de producto debe ser un número: " + codigoProducto);
    }
    return inventarioRepo.findByProducto_Id(codigoProductoId);
  }

  @Transactional
  public void eliminar(Long idItem) {
    if (!inventarioRepo.existsById(idItem)) {
      throw new IllegalArgumentException("InventarioItem no existe: " + idItem);
    }
    inventarioRepo.deleteById(idItem);
  }

  @Transactional(readOnly = true)
  public List<Inventario> listarTodos() {
    return inventarioRepo.findAll();
  }
}
