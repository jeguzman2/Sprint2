package com.provesi.demo.security;

import org.springframework.stereotype.Component;

@Component
public class MaliciusInputValidator {
    public boolean contieneAtaque(String texto) {
        if (texto == null) return false;

        String lower = texto.toLowerCase();

        // patrones típicos de SQL injection
        return lower.contains(" or ")
            || lower.contains(" and ")
            || lower.contains("1=1")
            || lower.contains("--")
            || lower.contains("/*")
            || lower.contains("*/")
            || lower.contains(" drop ")
            || lower.contains(" delete ")
            || lower.contains(" update ")
            || lower.contains(" insert ")
            || lower.contains(" union ");
    }
}
