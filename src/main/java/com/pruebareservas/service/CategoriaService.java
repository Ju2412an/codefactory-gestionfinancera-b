package com.pruebareservas.service;

import com.pruebareservas.dto.CategoriaDTO;
import com.pruebareservas.entity.CategoriaEntity;
import com.pruebareservas.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public CategoriaEntity crearCategoria(CategoriaDTO dto) {

        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setNombre(dto.getNombre());
        categoria.setTipo(dto.getTipo());

        return categoriaRepository.save(categoria);
    }

    public List<CategoriaEntity> listarCategorias() {
        return categoriaRepository.findAll();
    }
}
