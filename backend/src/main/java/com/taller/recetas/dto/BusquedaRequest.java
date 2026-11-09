package com.taller.recetas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BusquedaRequest(
        @NotBlank(message = "Escribe al menos un ingrediente")
        @Size(max = 300, message = "Los ingredientes no pueden pasar de 300 caracteres")
        String ingredientes
) {
}
