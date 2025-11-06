package com.provesi.demo.repositorios;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.provesi.demo.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);
}
