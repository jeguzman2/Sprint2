package com.provesi.demo.DTO;

import com.provesi.demo.model.EstadoAlistamiento;

public record OrdenAlistamientoDTO (
    EstadoAlistamiento estadoInicial,
    Long operarioId
    
) {}
