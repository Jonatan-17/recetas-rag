package com.taller.recetas.repository;

import com.taller.recetas.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    Optional<Favorito> findByRecetaId(Long recetaId);

    List<Favorito> findAllByOrderByGuardadoEnDesc();

    boolean existsByRecetaId(Long recetaId);

    List<Favorito> findByRecetaIdIn(Set<Long> recetaIds);
}
