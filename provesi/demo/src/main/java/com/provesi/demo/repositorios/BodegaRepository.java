package com.provesi.demo.repositorios;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.provesi.demo.model.Bodega;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    
}