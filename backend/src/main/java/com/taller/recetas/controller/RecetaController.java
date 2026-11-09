package com.taller.recetas.controller;

import com.taller.recetas.dto.RecetaDetalle;
import com.taller.recetas.service.RecetaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping("/{id}")
    public RecetaDetalle obtener(@PathVariable Long id) {
        return recetaService.obtener(id);
    }
}
