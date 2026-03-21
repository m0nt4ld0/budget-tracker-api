package com.mmontaldo.budget_tracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;

public interface MovimientoService {
    public MovimientoDto crearGasto(MovimientoDto gastoDto);
    public Page<MovimientoDto> getGastos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable);
    public Map<String, BigDecimal> getTotalesPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta);
    public Page<MovimientoDto> getGastosFiltradosPorPagina(
                LocalDate fechaDesde,
                LocalDate fechaHasta,
                Long categoriaId,
                Pageable pageable
        ) ;
}
