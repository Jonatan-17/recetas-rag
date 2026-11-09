package com.taller.recetas.dto;

import java.util.List;

public record RecetaDetalle(
        Long id,
        String nombre,
        String descripcion,
        int tiempoMinutos,
        List<String> ingredientes,
        List<String> pasos,
        boolean favorita
) {
}
