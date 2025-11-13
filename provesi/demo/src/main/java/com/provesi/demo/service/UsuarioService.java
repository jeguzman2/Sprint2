package com.provesi.demo.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.provesi.demo.model.Usuario;
import com.provesi.demo.repositorios.UsuarioRepository;
import com.provesi.demo.security.MaliciusInputValidator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import javax.swing.text.html.parser.Entity;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepo;
  private final MaliciusInputValidator validator;

  @PersistenceContext
  private EntityManager entityManager;

  public UsuarioService(UsuarioRepository usuarioRepo, MaliciusInputValidator validator) {
    this.usuarioRepo = usuarioRepo;
    this.validator = validator;
  }

  @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + usuario.getEmail());
        }
        return usuarioRepo.save(usuario);
    }

  @Transactional
  public Usuario actualizar(Long idUsuario, Usuario cambios) {
    var existente = usuarioRepo.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

    // Solo actualiza lo que venga en el body porque aja 
    if (cambios.getNombre() != null) {
        existente.setNombre(cambios.getNombre());
    }

    if (cambios.getEmail() != null) {
        // validar email unique
        if (!cambios.getEmail().equals(existente.getEmail()) &&
                usuarioRepo.existsByEmail(cambios.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + cambios.getEmail());
        }
        existente.setEmail(cambios.getEmail());
    }

    if (cambios.getEstado() != null) {
        existente.setEstado(cambios.getEstado());
    }

    if (cambios.getRol() != null) {
        existente.setRol(cambios.getRol());
    }

    return usuarioRepo.save(existente);

  }

  @Transactional(readOnly = true)
  public Usuario obtener(Long id) {
    if (id != null) {

      return usuarioRepo.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }
    return null;
  }

  @Transactional(readOnly = true)
  public Usuario obtenerPorEmail(String email) {
    return usuarioRepo.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado por email: " + email));
  }

  @Transactional(readOnly = true)
  public List<Usuario> listar() {
    return usuarioRepo.findAll();
  }

  @Transactional
  public void eliminar(Long id) {
    if (id == null || !usuarioRepo.existsById(id)) {
      throw new IllegalArgumentException("Usuario no existe: " + id);
    }

    usuarioRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<Usuario> buscarVulnerable(String idRaw) {
    if (validator.contieneAtaque(idRaw)) {
        throw new IllegalArgumentException("Input contains malicious content");
    }
      // AQUÍ concatenamos el valor directamente => vulnerable a SQL Injection
      String sql = "SELECT * FROM usuarios WHERE id_usuario = " + idRaw;
      return entityManager.createNativeQuery(sql, Usuario.class).getResultList();
  }
}