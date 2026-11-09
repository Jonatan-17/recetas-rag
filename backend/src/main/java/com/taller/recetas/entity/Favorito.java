package com.taller.recetas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false, unique = true)
    private Receta receta;

    @Column(nullable = false)
    private LocalDateTime guardadoEn;

    protected Favorito() {
    }

    public Favorito(Receta receta) {
        this.receta = receta;
        this.guardadoEn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Receta getReceta() {
        return receta;
    }

    public LocalDateTime getGuardadoEn() {
        return guardadoEn;
    }
}
