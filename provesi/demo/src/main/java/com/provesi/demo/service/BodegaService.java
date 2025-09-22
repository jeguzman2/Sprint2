package com.provesi.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.provesi.demo.model.Bodega;
import com.provesi.demo.repositorios.BodegaRepository;
import java.util.List;

@Service
public class BodegaService {

  private final BodegaRepository bodegaRepo;

  public BodegaService(BodegaRepository bodegaRepo) {
    this.bodegaRepo = bodegaRepo;
  }

  @Transactional
  public Bodega crear(Bodega b) {
    return bodegaRepo.save(b);
  }

  @Transactional
  public Bodega actualizar(Long idBodega, Bodega cambios) {
    Bodega existente = bodegaRepo.findById(idBodega)
        .orElseThrow(() -> new IllegalArgumentException("Bodega no encontrada: " + idBodega));

    existente.setNombre(cambios.getNombre());
    existente.setCiudad(cambios.getCiudad());

    return bodegaRepo.save(existente);
  }

  @Transactional(readOnly = true)
  public Bodega obtener(Long id) {
    return bodegaRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Bodega no encontrada: " + id));
  }

  @Transactional(readOnly = true)
  public List<Bodega> listar() {
    return bodegaRepo.findAll();
  }

  @Transactional
  public void eliminar(Long id) {
    if (!bodegaRepo.existsById(id)) {
      throw new IllegalArgumentException("Bodega no existe: " + id);
    }
    bodegaRepo.deleteById(id);
  }
}