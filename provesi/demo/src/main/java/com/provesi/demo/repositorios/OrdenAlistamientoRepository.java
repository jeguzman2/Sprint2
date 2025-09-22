package com.provesi.demo.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;

import com.provesi.demo.model.EstadoAlistamiento;
import com.provesi.demo.model.OrdenAlistamiento;
import java.util.List;

public interface OrdenAlistamientoRepository extends JpaRepository<OrdenAlistamiento, Long> {
  
  // Buscar todas las órdenes por estado (Ej: "ALISTAMIENTO", "VERIFICADO", etc.)
  List<OrdenAlistamiento> findByEstado(EstadoAlistamiento estado);

  // Si quieres filtrar por operario asignado
  List<OrdenAlistamiento> findByOperarioIdUsuario(Long idUsuario);

  List<OrdenAlistamiento> findByEstadoIn(Iterable<EstadoAlistamiento> estados);
}