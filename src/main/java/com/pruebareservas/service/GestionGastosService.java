package com.pruebareservas.service;

import com.pruebareservas.dto.MovimientoDTO;
import com.pruebareservas.entity.*;
import com.pruebareservas.repository.*;
import org.springframework.stereotype.Service;

@Service
public class GestionGastosService {

    private final MovimientoRepository movimientoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PresupuestoRepository presupuestoRepository;

    public GestionGastosService(MovimientoRepository movimientoRepository,
                                CategoriaRepository categoriaRepository,
                                PresupuestoRepository presupuestoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.categoriaRepository = categoriaRepository;
        this.presupuestoRepository = presupuestoRepository;
    }

    public PresupuestoEntity inicializar(double valor) {
        PresupuestoEntity p = new PresupuestoEntity();
        p.setTotal(valor);
        return presupuestoRepository.save(p);
    }

    public PresupuestoEntity registrarIngreso(MovimientoDTO dto) {

        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow();

        IngresoEntity ingreso = new IngresoEntity();
        ingreso.setValor(dto.getValor());
        ingreso.setCategoria(categoria);

        movimientoRepository.save(ingreso);

        PresupuestoEntity presupuesto = obtenerPresupuesto();

        presupuesto.setTotal(presupuesto.getTotal() + dto.getValor());

        return presupuestoRepository.save(presupuesto);
    }

public PresupuestoEntity registrarGasto(MovimientoDTO dto) {

    CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow();

    PresupuestoEntity presupuesto = obtenerPresupuesto();

    if (presupuesto.getTotal() < dto.getValor()) {
        throw new RuntimeException("No hay suficiente saldo para realizar el gasto");
    }

    GastoEntity gasto = new GastoEntity();
    gasto.setValor(dto.getValor());
    gasto.setCategoria(categoria);

    movimientoRepository.save(gasto);

    presupuesto.setTotal(presupuesto.getTotal() - dto.getValor());

    return presupuestoRepository.save(presupuesto);
}

    public PresupuestoEntity obtenerPresupuesto() {
        return presupuestoRepository.findAll().stream().findFirst().orElseThrow();
    }
}
