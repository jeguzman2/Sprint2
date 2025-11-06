package com.provesi.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.provesi.demo.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository <Pedido, Long> {

}
