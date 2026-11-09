package com.taller.recetas.service;

import com.taller.recetas.dto.RecetaDetalle;
import com.taller.recetas.dto.RecetaSugerida;
import com.taller.recetas.entity.Receta;
import com.taller.recetas.exception.RecursoNoEncontradoException;
import com.taller.recetas.repository.FavoritoRepository;
import com.taller.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final FavoritoRepository favoritoRepository;

    public RecetaService(RecetaRepository recetaRepository, FavoritoRepository favoritoRepository) {
        this.recetaRepository = recetaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    public RecetaDetalle obtener(Long id) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta " + id));

        return new RecetaDetalle(
                receta.getId(),
                receta.getNombre(),
                receta.getDescripcion(),
                receta.getTiempoMinutos(),
                List.copyOf(receta.getIngredientes()),
                List.copyOf(receta.getPasos()),
                favoritoRepository.existsByRecetaId(id));
    }

    public List<RecetaSugerida> sugerencias(Map<Long, Double> puntajes, String disponibles) {
        Map<Long, Receta> recetas = recetaRepository.findAllById(puntajes.keySet()).stream()
                .collect(Collectors.toMap(Receta::getId, Function.identity()));

        Set<Long> favoritas = favoritoRepository.findByRecetaIdIn(puntajes.keySet()).stream()
                .map(f -> f.getReceta().getId())
                .collect(Collectors.toSet());

        List<RecetaSugerida> resultado = new ArrayList<>();
        puntajes.forEach((id, puntaje) -> {
            Receta receta = recetas.get(id);
            if (receta == null) {
                return;
            }
            resultado.add(new RecetaSugerida(
                    receta.getId(),
                    receta.getNombre(),
                    receta.getDescripcion(),
                    receta.getTiempoMinutos(),
                    puntaje,
                    List.copyOf(receta.getIngredientes()),
                    ComparadorIngredientes.faltantes(receta.getIngredientes(), disponibles),
                    favoritas.contains(id)));
        });
        return resultado;
    }
}
