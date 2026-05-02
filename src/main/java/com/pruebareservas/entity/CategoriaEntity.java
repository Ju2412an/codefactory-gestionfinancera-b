package com.pruebareservas.entity;

import jakarta.persistence.*;

@Entity
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String tipo; // INGRESO o GASTO

    // getters y setters
}
