package com.mmontaldo.budget_tracker.service;

import java.util.List;

import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;

public interface CategoriaService {

    public List<CategoriaDto> getCategorias();

    public List<CategoriaDto> getCategoriasIngresos();

    public List<CategoriaDto> getCategoriasGastos();

    public CategoriaDto crearCategoriaIngreso(CategoriaDto dto);
    
    public CategoriaDto crearCategoriaGasto(CategoriaDto dto);

    public List<CategoriaDto> getCategoriasMovimientos();
}
