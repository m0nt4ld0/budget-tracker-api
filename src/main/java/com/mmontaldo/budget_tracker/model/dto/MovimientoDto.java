package com.mmontaldo.budget_tracker.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {
    private Long id;
    private LocalDate fecha;
    private CategoriaDto categoria;
    private String concepto;
    private BigDecimal importe;
    private String tipoMovimiento;
}
