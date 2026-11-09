package com.taller.recetas.service;

import com.taller.recetas.dto.BusquedaResponse;
import com.taller.recetas.dto.RecetaSugerida;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusquedaService {

    private final N8nClient n8nClient;
    private final RecetaService recetaService;

    public BusquedaService(N8nClient n8nClient, RecetaService recetaService) {
        this.n8nClient = n8nClient;
        this.recetaService = recetaService;
    }

    public BusquedaResponse buscar(String ingredientes) {
        String limpio = ingredientes.trim();
        N8nClient.RespuestaN8n respuesta = n8nClient.buscar(limpio);

        Map<Long, Double> puntajes = new LinkedHashMap<>();
        respuesta.recetas().forEach(r -> puntajes.put(r.id(), r.score()));

        List<RecetaSugerida> recetas = recetaService.sugerencias(puntajes, limpio);
        return new BusquedaResponse(limpio, respuesta.explicacion(), recetas);
    }
}
