package com.mmontaldo.budget_tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mmontaldo.budget_tracker.model.dto.MonedaDto;
import com.mmontaldo.budget_tracker.service.impl.MonedaServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/monedas")
@RequiredArgsConstructor
public class MonedaController {

    private final MonedaServiceImpl monedaService;
    
    @GetMapping
    public List<MonedaDto> getMonedas() {
        return monedaService.getMonedas();
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public MonedaDto crearMoneda(@RequestBody MonedaDto dto) {
        return monedaService.crearMoneda(dto);
    }
    
    @PatchMapping("/update/{id}")
    public MonedaDto editarMoneda(@PathVariable Long id, @RequestBody MonedaDto dto) {
        return monedaService.editarMoneda(id, dto);
    }

}
