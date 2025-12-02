package com.provesi.demo.security;

import com.provesi.demo.model.EstadoPedido;

public class InputSanitizer {

    // Validar ID
    public static void validateId(Long id) {
        if (id == null || id <= 0 || id > 999999999L) {
            throw new ValidationException("ID inválido o sospechoso.");
        }
    }

    // Validar el total del pedido
    public static void validateTotal(float total) {
        if (total < 0 || total > 10_000_000) {
            throw new ValidationException("Valor de total inválido o fuera de rango.");
        }
    }

    // Validar el estado
    public static void validateEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new ValidationException("Estado vacío.");
        }

        if (!estado.matches("^[A-Z_]+$")) {
            throw new ValidationException("Estado contiene caracteres inválidos.");
        }
        
        // Verifica si es un estado válido
        try {
            EstadoPedido.valueOf(estado);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Estado '" + estado + "' no permitido. Usa: ACTIVO, EN_PROCESO, EN_CAMINO o FINALIZADO");
        }
    }
}
