package com.pruebareservas.controller;

import com.pruebareservas.dto.*;
import com.pruebareservas.entity.PresupuestoEntity;
import com.pruebareservas.service.GestionGastosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GestionGastosController {

    private final GestionGastosService service;

    public GestionGastosController(GestionGastosService service) {
        this.service = service;
    }

    @PostMapping("/inicializar/{valor}")
    public PresupuestoEntity inicializar(
            @PathVariable double valor,
            @RequestParam Long usuarioId) {

        return service.inicializar(usuarioId, valor);
    }

    @PostMapping("/ingreso")
    public PresupuestoEntity ingreso(
            @RequestBody MovimientoDTO dto,
            @RequestParam Long usuarioId) {

        return service.registrarIngreso(usuarioId, dto);
    }

    @PostMapping("/gasto")
    public PresupuestoEntity gasto(
            @RequestBody MovimientoDTO dto,
            @RequestParam Long usuarioId) {

        return service.registrarGasto(usuarioId, dto);
    }

    @GetMapping
    public PresupuestoEntity obtener(
            @RequestParam Long usuarioId) {

        return service.obtenerPresupuesto(usuarioId);
    }

    @GetMapping("/movimientos")
    public List<MovimientoDTO> movimientos(
            @RequestParam Long usuarioId) {

        return service.listarMovimientos(usuarioId);
    }

    @GetMapping("/balance-mensual")
    public BalanceMensualDTO balanceMensual(
            @RequestParam Long usuarioId) {

        return service.obtenerBalanceMensual(usuarioId);
    }

    @GetMapping("/alerta-presupuesto")
    public AlertaPresupuestoDTO alertaPresupuesto(
            @RequestParam Long usuarioId) {

        return service.verificarAlertaPresupuesto(usuarioId);
    }

    @GetMapping("/recomendaciones")
    public RecomendacionDTO recomendaciones(
            @RequestParam Long usuarioId) {

        return service.obtenerRecomendaciones(usuarioId);
    }
}