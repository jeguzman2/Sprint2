package com.provesi.demo.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.provesi.demo.DTO.OrdenAlistamientoDTO; // <= OJO: paquete en minúscula "dto"
import com.provesi.demo.model.EstadoAlistamiento;
import com.provesi.demo.model.OrdenAlistamiento;
import com.provesi.demo.model.Usuario;
import com.provesi.demo.repositorios.UsuarioRepository;
import com.provesi.demo.service.OrdenAlistamientoService;

@RestController
@RequestMapping("/ordenes-alistamiento")
public class OrdenAlistamientoController {

    private final OrdenAlistamientoService ordenAlistamientoService;
    private final UsuarioRepository usuarioRepository;

    public OrdenAlistamientoController(OrdenAlistamientoService ordenAlistamientoService,
                                       UsuarioRepository usuarioRepository) {
        this.ordenAlistamientoService = ordenAlistamientoService;
        this.usuarioRepository = usuarioRepository;
    }

    
    @PostMapping
    public ResponseEntity<OrdenAlistamiento> crear(@RequestBody OrdenAlistamientoDTO dto) {
        OrdenAlistamiento orden = new OrdenAlistamiento();
        orden.setFechaCreacion(Instant.now());

        EstadoAlistamiento estado = dto.estadoInicial();
        orden.setEstado(estado);

        Optional<Usuario> posibleOperario = usuarioRepository.findById(dto.operarioId());
        Usuario operario = posibleOperario.orElseThrow(
                () -> new IllegalArgumentException("Operario no encontrado: " + dto.operarioId())
        );
        orden.setOperario(operario);

        OrdenAlistamiento creada = ordenAlistamientoService.crear(orden);
        return ResponseEntity.ok(creada);
    }

    
    @PatchMapping("/{id}/asignar-operario")
    public ResponseEntity<OrdenAlistamiento> asignarOperario(@PathVariable Long id,
                                                             @RequestParam Long idOperario) {
        OrdenAlistamiento orden = ordenAlistamientoService.asignarOperario(id, idOperario);
        return ResponseEntity.ok(orden);
    }

    /** Cambiar estado (p.ej., ALISTAMIENTO -> POR_VERIFICAR -> VERIFICADO ...) */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenAlistamiento> cambiarEstado(@PathVariable Long id,
                                                           @RequestParam String nuevoEstado) {
        OrdenAlistamiento orden = ordenAlistamientoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(orden);
    }

    /** Obtener por id */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenAlistamiento> obtener(@PathVariable Long id) {
        OrdenAlistamiento orden = ordenAlistamientoService.obtener(id);
        return ResponseEntity.ok(orden);
    }

    /** Listar por estado (query param String -> enum seguro) */
    @GetMapping("/por-estado")
    public ResponseEntity<List<OrdenAlistamiento>> listarPorEstado(@RequestParam String estado) {
        EstadoAlistamiento estadoEnum;
        try {
            estadoEnum = EstadoAlistamiento.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                "Estado inválido: " + estado + ". Valores permitidos: " +
                "ALISTAMIENTO, POR_VERIFICAR, VERIFICADO, RECHAZADO, EMPACADO, DESPACHADO, ENTREGADO"
            );
        }
        List<OrdenAlistamiento> lista = ordenAlistamientoService.listarPorEstado(estadoEnum);
        return ResponseEntity.ok(lista);
    }

    /** Listar por operario */
    @GetMapping("/por-operario/{idOperario}")
    public ResponseEntity<List<OrdenAlistamiento>> listarPorOperario(@PathVariable Long idOperario) {
        List<OrdenAlistamiento> lista = ordenAlistamientoService.listarPorOperario(idOperario);
        return ResponseEntity.ok(lista);
    }

    /** Listar todas */
    @GetMapping
    public ResponseEntity<List<OrdenAlistamiento>> listarTodas() {
        List<OrdenAlistamiento> lista = ordenAlistamientoService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    /** Eliminar */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenAlistamientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

