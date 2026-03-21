package com.mmontaldo.budget_tracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;

public interface MovimientoService {
    public MovimientoDto crearMovimiento(MovimientoDto movimientoDto);
    public Page<MovimientoDto> getMovimientos(LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable, TipoMovimiento tipoMovimiento);
    public Map<String, BigDecimal> getTotalesPorCategoria(LocalDate fechaDesde, LocalDate fechaHasta, TipoMovimiento tipoMovimiento);
    public Page<MovimientoDto> getMovimientosFiltradosPorPagina(
                LocalDate fechaDesde,
                LocalDate fechaHasta,
                Long categoriaId,
                TipoMovimiento tipoMovimiento,
                Pageable pageable
        ) ;
}
