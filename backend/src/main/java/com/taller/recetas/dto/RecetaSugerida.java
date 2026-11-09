package com.taller.recetas.dto;

import java.util.List;

public record RecetaSugerida(
        Long id,
        String nombre,
        String descripcion,
        int tiempoMinutos,
        double similitud,
        List<String> ingredientes,
        List<String> faltantes,
        boolean favorita
) {
}
