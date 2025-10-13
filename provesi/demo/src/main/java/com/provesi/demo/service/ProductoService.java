package com.provesi.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.provesi.demo.model.EstadoProducto;
import com.provesi.demo.model.Producto;
import com.provesi.demo.repositorios.ProductoRepository;

@Service
@Transactional
public class ProductoService {

  private final ProductoRepository productoRepo;

  public ProductoService(ProductoRepository productoRepo) {
    this.productoRepo = productoRepo;
  }

  

  @Transactional
  public Producto crear(Producto p) {
    return productoRepo.save(p);
  }

  @Transactional
  public Producto actualizar(Long id, Producto cambios) {
    Producto existente = obtenerPorId(id);

    if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
    if (cambios.getPeso() != null)   existente.setPeso(cambios.getPeso());   
    if (cambios.getUnidad() != null) existente.setUnidad(cambios.getUnidad());
    if (cambios.getEstado() != null) existente.setEstado(cambios.getEstado());
    if (cambios.getDescripcion() != null) existente.setDescripcion(cambios.getDescripcion());
    if (cambios.getDimensiones() != null) existente.setDimensiones(cambios.getDimensiones());

    Producto actualizado = productoRepo.save(existente);
    return actualizado;
}

  @Transactional(readOnly = true)
  public Producto obtenerPorId(Long id) {
    return productoRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
  }

  @Transactional(readOnly = true)
  public Producto obtenerPorNombre(String nombre) {
    return productoRepo.findByNombre(nombre)
        .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado por nombre: " + nombre));
  }

  @Transactional(readOnly = true)
  public List<Producto> listar() {
    return productoRepo.findAll();
  }

  // METODO PARA ACTUALIZAR EL ESTADO DEL PRODUCTO ASR2
  @Transactional
  public Producto actualizarEstado(Long id, EstadoProducto nuevoEstado) {
        // Buscar producto por ID
        Optional<Producto> productoOptional = productoRepo.findById(id);

        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setEstado(nuevoEstado); // cambiar estado
            return productoRepo.save(producto); // guardar cambio
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
    }


  @Transactional
  public void eliminar(Long id) {
    if (!productoRepo.existsById(id)) {
      throw new IllegalArgumentException("Producto no existe: " + id);
    }
    productoRepo.deleteById(id);
  }
}