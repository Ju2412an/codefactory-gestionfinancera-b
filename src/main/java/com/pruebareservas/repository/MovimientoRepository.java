package com.pruebareservas.repository;

import com.pruebareservas.entity.MovimientoEntity;
import com.pruebareservas.entity.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<MovimientoEntity, Long> {

    List<MovimientoEntity> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<MovimientoEntity> findByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    List<MovimientoEntity> findByUsuarioIdAndTipoAndFechaBetween(
            Long usuarioId,
            TipoMovimiento tipo,
            LocalDateTime inicio,
            LocalDateTime fin
    );
}
