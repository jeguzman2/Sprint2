package com.provesi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.provesi.demo.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findById(Long id);
  boolean existsByEmail(String email);
  Optional<Usuario> findByEmail(String email);
  
}