package com.pruebareservas.controller;

import com.pruebareservas.dto.MovimientoDTO;
import com.pruebareservas.entity.PresupuestoEntity;
import com.pruebareservas.service.GestionGastosService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gastos")
public class GestionGastosController {

    private final GestionGastosService service;

    public GestionGastosController(GestionGastosService service) {
        this.service = service;
    }

    @PostMapping("/inicializar/{valor}")
    public PresupuestoEntity inicializar(@PathVariable double valor) {
        return service.inicializar(valor);
    }

    @PostMapping("/ingreso")
    public PresupuestoEntity ingreso(@RequestBody MovimientoDTO dto) {
        return service.registrarIngreso(dto);
    }

    @PostMapping("/gasto")
    public PresupuestoEntity gasto(@RequestBody MovimientoDTO dto) {
        return service.registrarGasto(dto);
    }

    @GetMapping
    public PresupuestoEntity obtener() {
        return service.obtenerPresupuesto();
    }
}