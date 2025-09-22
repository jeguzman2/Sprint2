package com.provesi.demo.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.provesi.demo.model.Ubicacion;
import com.provesi.demo.repositorios.BodegaRepository;
import com.provesi.demo.repositorios.UbicacionRepository;
import com.provesi.demo.model.Bodega;

import java.util.List;

@Service
public class UbicacionService {

  private final UbicacionRepository ubicacionRepo;
  private final BodegaRepository bodegaRepo;

  public UbicacionService(UbicacionRepository ubicacionRepo, BodegaRepository bodegaRepo) {
    this.ubicacionRepo = ubicacionRepo;
    this.bodegaRepo = bodegaRepo;
  }

  @Transactional
  public Ubicacion crear(Ubicacion u, Long idBodega) {
    Bodega b = bodegaRepo.findById(idBodega)
        .orElseThrow(() -> new IllegalArgumentException("Bodega no encontrada: " + idBodega));
    u.setBodega(b);
    return ubicacionRepo.save(u);
  }

  @Transactional
  public Ubicacion actualizar(Long idUbicacion, Ubicacion cambios, Long idBodega) {
    var existente = ubicacionRepo.findById(idUbicacion)
        .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada: " + idUbicacion));

    if (idBodega != null) {
      var bodega = bodegaRepo.findById(idBodega)
          .orElseThrow(() -> new IllegalArgumentException("Bodega no encontrada: " + idBodega));
      existente.setBodega(bodega);
    }

    existente.setPasillo(cambios.getPasillo());
    existente.setEstante(cambios.getEstante());
    existente.setNivel(cambios.getNivel());

    return ubicacionRepo.save(existente);
  }

  @Transactional(readOnly = true)
  public Ubicacion obtener(Long id) {
    return ubicacionRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada: " + id));
  }

  @Transactional(readOnly = true)
  public List<Ubicacion> listar() {
    return ubicacionRepo.findAll();
  }

  @Transactional
  public void eliminar(Long id) {
    if (!ubicacionRepo.existsById(id)) {
      throw new IllegalArgumentException("Ubicación no existe: " + id);
    }
    ubicacionRepo.deleteById(id);
  }
}
