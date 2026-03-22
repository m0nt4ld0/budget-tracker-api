package com.mmontaldo.budget_tracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;
import com.mmontaldo.budget_tracker.service.impl.MovimientoServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingresos")
public class IngresoController {

    private final MovimientoServiceImpl movimientoService;

    @GetMapping("/")
    public Page<MovimientoDto> getGastosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = true) Long usuarioId,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return movimientoService.getMovimientos(fechaDesde, fechaHasta, TipoMovimiento.INGRESO, usuarioId, pageable);
    }

    @GetMapping("/filtro")
    public Page<MovimientoDto> getGastosFiltradosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = true) Long usuarioId,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return movimientoService.getMovimientosFiltradosPorPagina(fechaDesde, fechaHasta, categoriaId, TipoMovimiento.INGRESO, usuarioId, pageable);
    }

    @GetMapping("/por-categoria")
    public Map<String, BigDecimal> getTotalesPorCategoria(
            @RequestParam(required = true) LocalDate fechaDesde,
            @RequestParam(required = true) LocalDate fechaHasta,
            @RequestParam(required = true) Long usuarioId
    ) {
        return movimientoService.getTotalesPorCategoria(fechaDesde, fechaHasta, TipoMovimiento.INGRESO, usuarioId);
    }

}
