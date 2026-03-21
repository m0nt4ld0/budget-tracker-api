package com.mmontaldo.budget_tracker.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmontaldo.budget_tracker.model.TipoMovimiento;
import com.mmontaldo.budget_tracker.model.dto.MovimientoDto;
import com.mmontaldo.budget_tracker.service.impl.MovimientoServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movimientos")
public class MovimientoController {
    private final MovimientoServiceImpl movimientoService;

    @PostMapping("/crear")
    public MovimientoDto crearMovimiento(@RequestBody MovimientoDto movimientoDto) {
        movimientoDto.setTipoMovimiento(TipoMovimiento.INGRESO.name());
        return movimientoService.crearMovimiento(movimientoDto);
    }
}
