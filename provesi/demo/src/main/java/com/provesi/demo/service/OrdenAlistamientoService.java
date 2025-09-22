package com.provesi.demo.service;

import java.time.Instant;
import java.util.List;

import com.provesi.demo.model.EstadoAlistamiento;
import com.provesi.demo.model.OrdenAlistamiento;
import com.provesi.demo.model.Usuario;
import com.provesi.demo.repositorios.OrdenAlistamientoRepository;
import com.provesi.demo.repositorios.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenAlistamientoService {

    private final OrdenAlistamientoRepository ordenRepo;
    private final UsuarioRepository usuarioRepo;

    public OrdenAlistamientoService(OrdenAlistamientoRepository ordenRepo,
                                    UsuarioRepository usuarioRepo) {
        this.ordenRepo = ordenRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /** Crear una orden con estado inicial y operario asignado */
    @Transactional
    public OrdenAlistamiento crear(String estadoInicial, Long idOperario) {
        Usuario operario = usuarioRepo.findById(idOperario)
            .orElseThrow(() -> new IllegalArgumentException("Operario no encontrado: " + idOperario));

        OrdenAlistamiento orden = new OrdenAlistamiento();
        orden.setFechaCreacion(Instant.now());
        orden.setEstado(EstadoAlistamiento.valueOf(estadoInicial.toUpperCase()));
        orden.setOperario(operario);

        return ordenRepo.save(orden);
    }

    /** Crear una orden directamente desde un objeto */
    @Transactional
    public OrdenAlistamiento crear(OrdenAlistamiento orden) {
        return ordenRepo.save(orden);
    }

    /** Cambiar el estado de una orden */
    @Transactional
    public OrdenAlistamiento cambiarEstado(Long idAlistamiento, String nuevoEstado) {
        OrdenAlistamiento orden = ordenRepo.findById(idAlistamiento)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idAlistamiento));

        orden.setEstado(EstadoAlistamiento.valueOf(nuevoEstado.toUpperCase()));
        return ordenRepo.save(orden);
    }

    /** Asignar o cambiar el operario */
    @Transactional
    public OrdenAlistamiento asignarOperario(Long idAlistamiento, Long idOperario) {
        OrdenAlistamiento orden = ordenRepo.findById(idAlistamiento)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idAlistamiento));

        Usuario operario = usuarioRepo.findById(idOperario)
            .orElseThrow(() -> new IllegalArgumentException("Operario no encontrado: " + idOperario));

        orden.setOperario(operario);
        return ordenRepo.save(orden);
    }

    /** Obtener una orden por id */
    @Transactional(readOnly = true)
    public OrdenAlistamiento obtener(Long idAlistamiento) {
        return ordenRepo.findById(idAlistamiento)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + idAlistamiento));
    }

    /** Listar todas las órdenes */
    @Transactional(readOnly = true)
    public List<OrdenAlistamiento> listarTodas() {
        return ordenRepo.findAll();
    }

    /** Listar por estado */
    @Transactional(readOnly = true)
    public List<OrdenAlistamiento> listarPorEstado(EstadoAlistamiento estado) {
        return ordenRepo.findByEstado(estado);
    }

    /** Listar por id de operario */
    @Transactional(readOnly = true)
    public List<OrdenAlistamiento> listarPorOperario(Long idOperario) {
        return ordenRepo.findByOperarioIdUsuario(idOperario);
    }

    /** Eliminar una orden */
    @Transactional
    public void eliminar(Long idAlistamiento) {
        if (!ordenRepo.existsById(idAlistamiento)) {
            throw new IllegalArgumentException("Orden no existe: " + idAlistamiento);
        }
        ordenRepo.deleteById(idAlistamiento);
    }
}
