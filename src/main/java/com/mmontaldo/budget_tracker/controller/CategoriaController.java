package com.mmontaldo.budget_tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.service.impl.CategoriaServiceImpl;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaServiceImpl categoriaService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaDto> getCategoriasMovimientos() {
        return categoriaService.getCategoriasMovimientos();
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDto crearCategoriaGasto(@RequestBody CategoriaDto dto) {
        return categoriaService.crearCategoriaGasto(dto);
    }

    @PatchMapping("/update/{id}")
    public CategoriaDto editarCategoria(@PathVariable Long id, @RequestBody CategoriaDto dto) {
        return categoriaService.editarCategoria(id, dto);
    }

    @PatchMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}
