package com.mmontaldo.budget_tracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;
import com.mmontaldo.budget_tracker.service.impl.MovimientoServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gastos")
public class GastoController {

    private final MovimientoServiceImpl movimientoService;

    @GetMapping("/")
    public Page<MovimientoDto> getGastosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return movimientoService.getMovimientos(fechaDesde, fechaHasta, pageable, TipoMovimiento.EGRESO);
    }

    @GetMapping("/filtro")
    public Page<MovimientoDto> getGastosFiltradosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return movimientoService.getMovimientosFiltradosPorPagina(fechaDesde, fechaHasta, categoriaId, TipoMovimiento.EGRESO, pageable);
    }

    @GetMapping("/por-categoria")
    public Map<String, BigDecimal> getTotalesPorCategoria(
            @RequestParam(required = true) LocalDate fechaDesde,
            @RequestParam(required = true) LocalDate fechaHasta
    ) 
    {
        return movimientoService.getTotalesPorCategoria(fechaDesde, fechaHasta, TipoMovimiento.EGRESO);
    }

    @PostMapping("/crear")
    public MovimientoDto crearGasto(@RequestBody MovimientoDto gastoDto) {
        gastoDto.setTipoMovimiento(TipoMovimiento.EGRESO.name());
        return movimientoService.crearMovimiento(gastoDto);
    }
}

