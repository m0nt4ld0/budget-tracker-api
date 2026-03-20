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

import com.mmontaldo.budget_tracker.model.dto.GastoDto;
import com.mmontaldo.budget_tracker.service.impl.GastoServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gastos")
public class GastoController {

    private final GastoServiceImpl gastoService;

    @GetMapping("/")
    public Page<GastoDto> getGastosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return gastoService.getGastos(fechaDesde, fechaHasta, pageable);
    }

    @GetMapping("/filtro")
    public Page<GastoDto> getGastosFiltradosPorPagina(
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) Long categoriaId,
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return gastoService.getGastosFiltradosPorPagina(fechaDesde, fechaHasta, categoriaId, pageable);
    }

    @GetMapping("/por-categoria")
    public Map<String, BigDecimal> getTotalesPorCategoria(
            @RequestParam(required = true) LocalDate fechaDesde,
            @RequestParam(required = true) LocalDate fechaHasta
    ) {
        return gastoService.getTotalesPorCategoria(fechaDesde, fechaHasta);
    }

    @PostMapping("/crear")
    public GastoDto crearGasto(@RequestBody GastoDto gastoDto) {
        return gastoService.crearGasto(gastoDto);
    }
}

