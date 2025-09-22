package com.provesi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.provesi.demo.model.EstadoProducto;
import com.provesi.demo.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  
    Optional<Producto> findByNombre(String nombre);

    List<Producto> findByEstado(EstadoProducto estado);
    
}