package com.taller.recetas.controller;

import com.taller.recetas.dto.FavoritoRequest;
import com.taller.recetas.dto.FavoritoResponse;
import com.taller.recetas.service.FavoritoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @GetMapping
    public List<FavoritoResponse> listar() {
        return favoritoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoritoResponse guardar(@Valid @RequestBody FavoritoRequest request) {
        return favoritoService.guardar(request.recetaId());
    }

    @DeleteMapping("/{recetaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long recetaId) {
        favoritoService.eliminar(recetaId);
    }
}
