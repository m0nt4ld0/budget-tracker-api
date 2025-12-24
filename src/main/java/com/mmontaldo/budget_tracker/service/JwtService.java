package com.mmontaldo.budget_tracker.service;

public interface JwtService {
    String generateToken(String username);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
