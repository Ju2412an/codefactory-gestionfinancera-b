package com.pruebareservas.service;

import com.pruebareservas.dto.MovimientoDTO;
import com.pruebareservas.entity.*;
import com.pruebareservas.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public PresupuestoEntity inicializar(Long usuarioId, double valor) {
        PresupuestoEntity p = presupuestoRepository.findByUsuarioId(usuarioId)
                .orElseGet(PresupuestoEntity::new);
        p.setUsuarioId(usuarioId);
        p.setTotal(valor);
        return presupuestoRepository.save(p);
    }

    public PresupuestoEntity obtenerPresupuesto(Long usuarioId) {
        return presupuestoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Presupuesto no inicializado para el usuario"));
    }

    public PresupuestoEntity registrarIngreso(Long usuarioId, MovimientoDTO dto) {
        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        PresupuestoEntity presupuesto = obtenerPresupuesto(usuarioId);

        MovimientoEntity ingreso = new MovimientoEntity();
        ingreso.setTipo(TipoMovimiento.INGRESO);
        ingreso.setValor(dto.getValor());
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setCategoria(categoria);
        ingreso.setUsuarioId(usuarioId);
        movimientoRepository.save(ingreso);

        presupuesto.setTotal(presupuesto.getTotal() + dto.getValor());
        return presupuestoRepository.save(presupuesto);
    }

    public PresupuestoEntity registrarGasto(Long usuarioId, MovimientoDTO dto) {
        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        PresupuestoEntity presupuesto = obtenerPresupuesto(usuarioId);

        if (presupuesto.getTotal() < dto.getValor()) {
            throw new RuntimeException("No hay suficiente saldo para realizar el gasto");
        }

        MovimientoEntity gasto = new MovimientoEntity();
        gasto.setTipo(TipoMovimiento.GASTO);
        gasto.setValor(dto.getValor());
        gasto.setDescripcion(dto.getDescripcion());
        gasto.setCategoria(categoria);
        gasto.setUsuarioId(usuarioId);
        movimientoRepository.save(gasto);

        presupuesto.setTotal(presupuesto.getTotal() - dto.getValor());
        return presupuestoRepository.save(presupuesto);
    }

    public List<MovimientoDTO> listarMovimientos(Long usuarioId) {
        return movimientoRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MovimientoDTO toDTO(MovimientoEntity m) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(m.getId());
        dto.setTipo(m.getTipo());
        dto.setValor(m.getValor());
        dto.setDescripcion(m.getDescripcion());
        dto.setFecha(m.getFecha());
        if (m.getCategoria() != null) {
            dto.setCategoriaId(m.getCategoria().getId());
            dto.setCategoriaNombre(m.getCategoria().getNombre());
        }
        return dto;
    }
}
