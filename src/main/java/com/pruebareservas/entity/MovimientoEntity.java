package com.pruebareservas.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class MovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private double valor;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(optional = false)
    private CategoriaEntity categoria;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @PrePersist
    void onCreate() {
        if (fecha == null) fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public CategoriaEntity getCategoria() { return categoria; }
    public void setCategoria(CategoriaEntity categoria) { this.categoria = categoria; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
