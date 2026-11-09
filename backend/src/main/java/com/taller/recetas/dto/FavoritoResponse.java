package com.taller.recetas.dto;

import java.time.LocalDateTime;

public record FavoritoResponse(
        Long recetaId,
        String nombre,
        int tiempoMinutos,
        LocalDateTime guardadoEn
) {
}
