package com.mmontaldo.budget_tracker.service;

import java.util.List;

import com.mmontaldo.budget_tracker.model.dto.MonedaDto;

public interface MonedaService {
    public List<MonedaDto> getMonedas();
    public MonedaDto crearMoneda(MonedaDto dto);

    public MonedaDto editarMoneda(Long id, MonedaDto dto);
}
