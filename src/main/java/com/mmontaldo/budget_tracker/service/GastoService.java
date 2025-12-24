package com.mmontaldo.budget_tracker.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mmontaldo.budget_tracker.model.dto.GastoDto;

public interface GastoService {
    public GastoDto crearGasto(GastoDto gastoDto);
    public Page<GastoDto> getGastos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);
    public Map<String, Double> getTotalesPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta);
}
