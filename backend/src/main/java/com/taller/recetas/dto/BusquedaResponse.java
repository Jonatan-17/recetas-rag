package com.taller.recetas.dto;

import java.util.List;

public record BusquedaResponse(
        String ingredientes,
        String explicacion,
        List<RecetaSugerida> recetas
) {
}
