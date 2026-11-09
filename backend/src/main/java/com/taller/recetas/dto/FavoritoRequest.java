package com.taller.recetas.dto;

import jakarta.validation.constraints.NotNull;

public record FavoritoRequest(
        @NotNull(message = "Falta el id de la receta")
        Long recetaId
) {
}
