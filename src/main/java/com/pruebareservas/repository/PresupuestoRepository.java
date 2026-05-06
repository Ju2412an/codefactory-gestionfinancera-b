package com.pruebareservas.repository;

import com.pruebareservas.entity.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PresupuestoRepository extends JpaRepository<PresupuestoEntity, Long> {
    Optional<PresupuestoEntity> findByUsuarioId(Long usuarioId);
}
