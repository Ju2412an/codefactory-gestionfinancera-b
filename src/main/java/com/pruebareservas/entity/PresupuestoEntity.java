package com.pruebareservas.entity;

import jakarta.persistence.*;

@Entity
public class PresupuestoEntity {

    private Long id;
    private double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
