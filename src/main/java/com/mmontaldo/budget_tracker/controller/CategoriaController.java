package com.mmontaldo.budget_tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;
import com.mmontaldo.budget_tracker.service.impl.CategoriaServiceImpl;

@RestController
@RequestMapping("/api/categorias/")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaServiceImpl categoriaService;
    
    @GetMapping
    public List<CategoriaDto> getCategorias() {
        return categoriaService.getCategorias();
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDto crearCategoria(@RequestBody CategoriaDto dto) {
        return categoriaService.crearCategoria(dto);
    }
}
