package com.mmontaldo.budget_tracker.model.response;

public record AuthResponseDto(Long id, String username, String token, String nombre, String imagenUrl, Boolean activo) {
}
