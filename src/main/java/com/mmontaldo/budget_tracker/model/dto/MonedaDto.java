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
public class MonedaDto {
    private Long id;
    private String codMoneda;
    private String descMoneda;
}
