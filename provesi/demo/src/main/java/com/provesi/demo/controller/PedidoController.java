package com.provesi.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.provesi.demo.DTO.PedidoRequest;
import com.provesi.demo.model.EstadoPedido;
import com.provesi.demo.model.Pedido;
import com.provesi.demo.security.InputSanitizer;
import com.provesi.demo.security.ValidationException;
import com.provesi.demo.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // POST - Crear pedido con DTO
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequest request) {
        try {
            //  Validaciones de entrada
            InputSanitizer.validateTotal(request.getTotal());
            InputSanitizer.validateEstado(request.getEstado());

            // Convertir DTO → Entidad
            Pedido pedido = new Pedido();
            pedido.setTotal(request.getTotal());
            pedido.setEstado(EstadoPedido.valueOf(request.getEstado()));

            Pedido nuevoPedido = pedidoService.crearPedido(pedido);
            return ResponseEntity.ok(nuevoPedido);

        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET - Listar pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    // PUT - Cambiar estado del pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {

        try {
            //  Validación de ID
            InputSanitizer.validateId(id);

            //  Validar estado entrada (String)
            InputSanitizer.validateEstado(nuevoEstado);

            // Convertir string → Enum
            EstadoPedido estadoEnum = EstadoPedido.valueOf(nuevoEstado);

            Pedido actualizado = pedidoService.cambiarEstado(id, estadoEnum);
            return ResponseEntity.ok(actualizado);

        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
