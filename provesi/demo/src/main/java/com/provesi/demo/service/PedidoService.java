package com.provesi.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.provesi.demo.model.EstadoPedido;
import com.provesi.demo.model.Pedido;
import com.provesi.demo.repositorios.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Buscar pedido
    public Optional<Pedido> obtenerPedidoPorId(int idPedido) {
        return pedidoRepository.findById((long) idPedido);
    }


    // Crear un pedido
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.ACTIVO);
        }
        return pedidoRepository.save(pedido);
    }

    // Obtener todos los pedidos
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    // Cambiar estado del pedido
    @Transactional
    public Pedido cambiarEstado(int idPedido, String nuevoEstado) {
    Pedido pedido = pedidoRepository.findById((long) idPedido)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

    // Convertir el String recibido al enum EstadoPedido
    try {
        EstadoPedido estadoEnum = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
        pedido.setEstado(estadoEnum);
    } catch (IllegalArgumentException e) {
        throw new RuntimeException("Estado inválido: " + nuevoEstado);
    }

    return pedidoRepository.save(pedido);
}
}
