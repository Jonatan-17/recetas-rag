package com.taller.recetas.repository;

import com.taller.recetas.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
}
