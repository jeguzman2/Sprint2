package com.provesi.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EstadoAlistamiento {
    ALISTAMIENTO,
    POR_VERIFICAR,
    VERIFICADO,
    RECHAZADO,
    EMPACADO,
    DESPACHADO,
    ENTREGADO;

    @JsonCreator
    public static EstadoAlistamiento from(String value) {
        return value == null ? null : EstadoAlistamiento.valueOf(value.trim().toUpperCase());
    }
}

