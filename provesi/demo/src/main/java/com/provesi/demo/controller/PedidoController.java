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

import com.provesi.demo.model.EstadoPedido;
import com.provesi.demo.model.Pedido;
import com.provesi.demo.security.InputSanitizer;
import com.provesi.demo.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    //POST - Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
    // Validar total del pedido
        InputSanitizer.validateTotal(pedido.getTotal());

        // Validar estado del pedido
        InputSanitizer.validateEstado(pedido.getEstado().toString());

        Pedido nuevoPedido = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(nuevoPedido);
    }

    // GET - Listar todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        return ResponseEntity.ok(pedidos);
    }

    // Cambiar estado con PUT (ASR3- Disponibilidad)
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> cambiarEstado(
        @PathVariable Long id,
        @RequestParam EstadoPedido nuevoEstado) {

        // Validar ID
        InputSanitizer.validateId(id);

        // Validar estado
        InputSanitizer.validateEstado(nuevoEstado.toString());

        Pedido actualizado = pedidoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }
}
