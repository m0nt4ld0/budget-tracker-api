package com.mmontaldo.budget_tracker.exception;

public class GastoImporteNegativoException extends RuntimeException {
    public GastoImporteNegativoException(String message) {
        super(message);
    }
}