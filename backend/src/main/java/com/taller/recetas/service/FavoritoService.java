package com.taller.recetas.service;

import com.taller.recetas.dto.FavoritoResponse;
import com.taller.recetas.entity.Favorito;
import com.taller.recetas.entity.Receta;
import com.taller.recetas.exception.RecursoDuplicadoException;
import com.taller.recetas.exception.RecursoNoEncontradoException;
import com.taller.recetas.repository.FavoritoRepository;
import com.taller.recetas.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final RecetaRepository recetaRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, RecetaRepository recetaRepository) {
        this.favoritoRepository = favoritoRepository;
        this.recetaRepository = recetaRepository;
    }

    @Transactional(readOnly = true)
    public List<FavoritoResponse> listar() {
        return favoritoRepository.findAllByOrderByGuardadoEnDesc().stream()
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional
    public FavoritoResponse guardar(Long recetaId) {
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la receta " + recetaId));

        if (favoritoRepository.existsByRecetaId(recetaId)) {
            throw new RecursoDuplicadoException("La receta ya está en favoritos");
        }
        return aRespuesta(favoritoRepository.save(new Favorito(receta)));
    }

    @Transactional
    public void eliminar(Long recetaId) {
        Favorito favorito = favoritoRepository.findByRecetaId(recetaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La receta " + recetaId + " no está en favoritos"));
        favoritoRepository.delete(favorito);
    }

    private FavoritoResponse aRespuesta(Favorito favorito) {
        Receta receta = favorito.getReceta();
        return new FavoritoResponse(receta.getId(), receta.getNombre(), receta.getTiempoMinutos(), favorito.getGuardadoEn());
    }
}
