package com.provesi.demo.security;

public class InputSanitizer {

    // Validar ID
    public static void validateId(Long id) {
        if (id == null || id <= 0 || id > 999999999L) {
            throw new IllegalArgumentException("ID inválido o sospechoso.");
        }
    }

    // Validar el total del pedido
    public static void validateTotal(float total) {
        if (total < 0 || total > 10_000_000) {
            throw new IllegalArgumentException("Valor de total inválido o fuera de rango.");
        }
    }

    // Validar el estado
    public static void validateEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("Estado vacío.");
        }

        // Solo permitir letras
        if (!estado.matches("^[A-Z_]+$")) {
            throw new IllegalArgumentException("Estado contiene caracteres inválidos.");
        }
    }
}
