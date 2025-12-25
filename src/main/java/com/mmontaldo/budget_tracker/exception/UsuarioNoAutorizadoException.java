package com.mmontaldo.budget_tracker.exception;

public class UsuarioNoAutorizadoException extends AuthException {
    public UsuarioNoAutorizadoException(String message) {
        super(message);
    }
}
