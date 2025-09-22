package com.provesi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.provesi.demo.model.Ubicacion;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}