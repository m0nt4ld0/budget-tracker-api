package com.mmontaldo.budget_tracker.exception;

public class MonedaNotFoundException extends RuntimeException {
    public MonedaNotFoundException(String message) {
        super(message);
    }
}