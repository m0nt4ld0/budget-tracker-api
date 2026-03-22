package com.mmontaldo.budget_tracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;

public interface MovimientoService {
    MovimientoDto crearMovimiento(MovimientoDto movimientoDto);
    
    Page<MovimientoDto> getMovimientos(
        LocalDate fechaDesde, 
        LocalDate fechaHasta, 
        TipoMovimiento tipoMovimiento, 
        Long usuarioId,
        Pageable pageable
    );
    
    Map<String, BigDecimal> getTotalesPorCategoria(
        LocalDate fechaDesde, 
        LocalDate fechaHasta, 
        TipoMovimiento tipoMovimiento, 
        Long usuarioId
    );
    
    Page<MovimientoDto> getMovimientosFiltradosPorPagina(
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Long categoriaId,
        TipoMovimiento tipoMovimiento,
        Long usuarioId,
        Pageable pageable
    );
}