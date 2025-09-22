package com.provesi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.provesi.demo.model.Inventario;
import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
  List<Inventario> findByProducto_Id(long productoId);
}