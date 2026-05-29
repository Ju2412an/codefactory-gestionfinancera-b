package com.pruebareservas.service;

import com.pruebareservas.dto.*;
import com.pruebareservas.entity.*;
import com.pruebareservas.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestionGastosService {

    private final MovimientoRepository movimientoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PresupuestoRepository presupuestoRepository;

    public GestionGastosService(
            MovimientoRepository movimientoRepository,
            CategoriaRepository categoriaRepository,
            PresupuestoRepository presupuestoRepository
    ) {
        this.movimientoRepository = movimientoRepository;
        this.categoriaRepository = categoriaRepository;
        this.presupuestoRepository = presupuestoRepository;
    }

    public PresupuestoEntity inicializar(Long usuarioId, double valor) {

        PresupuestoEntity p = presupuestoRepository.findByUsuarioId(usuarioId)
                .orElseGet(PresupuestoEntity::new);

        p.setUsuarioId(usuarioId);
        p.setTotal(valor);

        return presupuestoRepository.save(p);
    }

    public PresupuestoEntity obtenerPresupuesto(Long usuarioId) {

        return presupuestoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Presupuesto no inicializado para el usuario"));
    }

    public PresupuestoEntity registrarIngreso(Long usuarioId, MovimientoDTO dto) {

        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() ->
                        new RuntimeException("Categoría no encontrada"));

        PresupuestoEntity presupuesto = obtenerPresupuesto(usuarioId);

        MovimientoEntity ingreso = new MovimientoEntity();

        ingreso.setTipo(TipoMovimiento.INGRESO);
        ingreso.setValor(dto.getValor());
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setCategoria(categoria);
        ingreso.setUsuarioId(usuarioId);

        movimientoRepository.save(ingreso);

        presupuesto.setTotal(
                presupuesto.getTotal() + dto.getValor()
        );

        return presupuestoRepository.save(presupuesto);
    }

    public PresupuestoEntity registrarGasto(Long usuarioId, MovimientoDTO dto) {

        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() ->
                        new RuntimeException("Categoría no encontrada"));

        PresupuestoEntity presupuesto = obtenerPresupuesto(usuarioId);

        if (presupuesto.getTotal() < dto.getValor()) {
            throw new RuntimeException(
                    "No hay suficiente saldo para realizar el gasto"
            );
        }

        MovimientoEntity gasto = new MovimientoEntity();

        gasto.setTipo(TipoMovimiento.GASTO);
        gasto.setValor(dto.getValor());
        gasto.setDescripcion(dto.getDescripcion());
        gasto.setCategoria(categoria);
        gasto.setUsuarioId(usuarioId);

        movimientoRepository.save(gasto);

        presupuesto.setTotal(
                presupuesto.getTotal() - dto.getValor()
        );

        return presupuestoRepository.save(presupuesto);
    }

    public List<MovimientoDTO> listarMovimientos(Long usuarioId) {

        return movimientoRepository
                .findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BalanceMensualDTO obtenerBalanceMensual(Long usuarioId) {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicioMes =
                hoy.withDayOfMonth(1).atStartOfDay();

        LocalDateTime finMes =
                hoy.withDayOfMonth(hoy.lengthOfMonth())
                        .atTime(23,59,59);

        double ingresos = movimientoRepository
                .findByUsuarioIdAndTipoAndFechaBetween(
                        usuarioId,
                        TipoMovimiento.INGRESO,
                        inicioMes,
                        finMes
                )
                .stream()
                .mapToDouble(MovimientoEntity::getValor)
                .sum();

        double gastos = movimientoRepository
                .findByUsuarioIdAndTipoAndFechaBetween(
                        usuarioId,
                        TipoMovimiento.GASTO,
                        inicioMes,
                        finMes
                )
                .stream()
                .mapToDouble(MovimientoEntity::getValor)
                .sum();

        PresupuestoEntity presupuesto =
                obtenerPresupuesto(usuarioId);

        BalanceMensualDTO dto = new BalanceMensualDTO();

        dto.setTotalIngresos(ingresos);
        dto.setTotalGastos(gastos);
        dto.setSaldoActual(presupuesto.getTotal());

        return dto;
    }

    public AlertaPresupuestoDTO verificarAlertaPresupuesto(Long usuarioId) {

        BalanceMensualDTO balance =
                obtenerBalanceMensual(usuarioId);

        double porcentaje =
                (balance.getTotalGastos() /
                        balance.getSaldoActual()) * 100;

        AlertaPresupuestoDTO dto =
                new AlertaPresupuestoDTO();

        dto.setTotalGastado(balance.getTotalGastos());
        dto.setPresupuestoActual(balance.getSaldoActual());
        dto.setPorcentajeGastado(porcentaje);

        if (porcentaje >= 50) {

            dto.setAlertaActiva(true);

            dto.setMensaje(
                    "ALERTA: Has superado el 50% de tu presupuesto mensual."
            );

        } else {

            dto.setAlertaActiva(false);

            dto.setMensaje(
                    "Tu nivel de gastos está dentro del rango permitido."
            );
        }

        return dto;
    }

    public RecomendacionDTO obtenerRecomendaciones(Long usuarioId) {

        BalanceMensualDTO balance =
                obtenerBalanceMensual(usuarioId);

        List<String> recomendaciones =
                new ArrayList<>();

        if (balance.getTotalGastos() >
                balance.getTotalIngresos()) {

            recomendaciones.add(
                    "Tus gastos son mayores que tus ingresos. Considera reducir gastos innecesarios."
            );
        }

        if (balance.getSaldoActual() < 100000) {

            recomendaciones.add(
                    "Tu saldo disponible es bajo. Procura controlar tus próximos gastos."
            );
        }

        if (balance.getTotalGastos() >
                (balance.getTotalIngresos() * 0.7)) {

            recomendaciones.add(
                    "Tus gastos consumen más del 70% de tus ingresos mensuales."
            );
        }

        if (recomendaciones.isEmpty()) {

            recomendaciones.add(
                    "Buen manejo financiero. Continúa manteniendo un balance saludable."
            );
        }

        RecomendacionDTO dto =
                new RecomendacionDTO();

        dto.setRecomendaciones(recomendaciones);

        return dto;
    }

    private MovimientoDTO toDTO(MovimientoEntity movimiento) {

        MovimientoDTO dto = new MovimientoDTO();

        dto.setId(movimiento.getId());
        dto.setTipo(movimiento.getTipo());
        dto.setValor(movimiento.getValor());
        dto.setDescripcion(movimiento.getDescripcion());
        dto.setFecha(movimiento.getFecha());

        if (movimiento.getCategoria() != null) {

            dto.setCategoriaId(
                    movimiento.getCategoria().getId()
            );

            dto.setCategoriaNombre(
                    movimiento.getCategoria().getNombre()
            );
        }

        return dto;
    }
}
