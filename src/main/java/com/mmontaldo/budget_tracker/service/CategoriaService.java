package com.mmontaldo.budget_tracker.service;

import java.util.List;

import com.mmontaldo.budget_tracker.model.dto.CategoriaDto;

public interface CategoriaService {

    public List<CategoriaDto> getCategorias();

    public CategoriaDto crearCategoria(CategoriaDto dto);
}
