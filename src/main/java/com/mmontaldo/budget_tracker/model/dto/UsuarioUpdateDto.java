package com.mmontaldo.budget_tracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateDto {
    private String nombre;
    private String email;
    private String imagenUrl;
    private Boolean activo;    
}
