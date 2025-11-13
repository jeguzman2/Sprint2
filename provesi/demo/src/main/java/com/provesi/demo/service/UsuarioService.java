package com.provesi.demo.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.provesi.demo.model.Usuario;
import com.provesi.demo.repositorios.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepo;

  public UsuarioService(UsuarioRepository usuarioRepo) {
    this.usuarioRepo = usuarioRepo;
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
    if (idUsuario != null)
    {
      var existente = usuarioRepo.findById(idUsuario)
          .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));
      existente.setNombre(cambios.getNombre());
      existente.setEmail(cambios.getEmail());
      existente.setEstado(cambios.getEstado());
      return usuarioRepo.save(existente);
    }

    // (El rol sólo si lo tienes en la entidad; si no, omítelo)
    return null;

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
}