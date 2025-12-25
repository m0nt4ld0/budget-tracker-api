package com.mmontaldo.budget_tracker.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDto {

    private Long id;
    private String categoria;
    private String icono;
    private Boolean activo;
}
