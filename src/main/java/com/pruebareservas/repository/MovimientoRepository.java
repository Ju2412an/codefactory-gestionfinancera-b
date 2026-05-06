package com.pruebareservas.repository;

import com.pruebareservas.entity.MovimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {
    List<MovimientoEntity> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
