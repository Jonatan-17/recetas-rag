package com.taller.recetas.controller;

import com.taller.recetas.dto.BusquedaRequest;
import com.taller.recetas.dto.BusquedaResponse;
import com.taller.recetas.service.BusquedaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/busquedas")
public class BusquedaController {

    private final BusquedaService busquedaService;

    public BusquedaController(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    @PostMapping
    public BusquedaResponse buscar(@Valid @RequestBody BusquedaRequest request) {
        return busquedaService.buscar(request.ingredientes());
    }
}
