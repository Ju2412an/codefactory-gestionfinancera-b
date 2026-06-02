package com.pruebareservas.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "presupuestos",
    uniqueConstraints = @UniqueConstraint(columnNames = "usuario_id")
)
public class PresupuestoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double total;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
