package com.mmontaldo.budget_tracker.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            Pageable pageable
    ) {
        return gastoService.getGastos(fechaDesde, fechaHasta, pageable);
    }

    @GetMapping("/por-categoria")
    public Map<String, Double> getTotalesPorCategoria(
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

