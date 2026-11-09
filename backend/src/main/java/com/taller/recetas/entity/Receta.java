package com.taller.recetas.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recetas")
public class Receta {

    // mismo id que en Qdrant
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    private int tiempoMinutos;

    @ElementCollection
    @CollectionTable(name = "receta_ingredientes", joinColumns = @JoinColumn(name = "receta_id"))
    @OrderColumn(name = "orden")
    @Column(name = "ingrediente", nullable = false)
    private List<String> ingredientes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "receta_pasos", joinColumns = @JoinColumn(name = "receta_id"))
    @OrderColumn(name = "orden")
    @Column(name = "paso", nullable = false, length = 500)
    private List<String> pasos = new ArrayList<>();

    protected Receta() {
    }

    public Receta(Long id, String nombre, String descripcion, int tiempoMinutos,
                  List<String> ingredientes, List<String> pasos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tiempoMinutos = tiempoMinutos;
        this.ingredientes = new ArrayList<>(ingredientes);
        this.pasos = new ArrayList<>(pasos);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTiempoMinutos() {
        return tiempoMinutos;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public List<String> getPasos() {
        return pasos;
    }
}
