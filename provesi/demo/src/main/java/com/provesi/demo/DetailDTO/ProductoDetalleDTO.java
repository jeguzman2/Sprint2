package com.provesi.demo.DetailDTO;

import com.provesi.demo.model.EstadoProducto;

public record ProductoDetalleDTO(
        String nombre,
        EstadoProducto estado,
        String bodega,
        String pasillo,
        String estante,
        String nivel,
        Integer cantidadDisponible
) {}