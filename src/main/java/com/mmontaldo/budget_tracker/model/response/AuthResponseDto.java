package com.mmontaldo.budget_tracker.model.response;

import java.time.LocalDateTime;

public record AuthResponseDto(String username, String token, LocalDateTime expires) {
}
