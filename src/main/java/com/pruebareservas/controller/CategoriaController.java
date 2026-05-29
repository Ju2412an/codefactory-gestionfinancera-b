package com.pruebareservas.controller;

import com.pruebareservas.dto.CategoriaDTO;
import com.pruebareservas.entity.CategoriaEntity;
import com.pruebareservas.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public CategoriaEntity crear(@RequestBody CategoriaDTO dto) {
        return categoriaService.crearCategoria(dto);
    }

    @GetMapping
    public List<CategoriaEntity> listar() {
        return categoriaService.listarCategorias();
    }
}
