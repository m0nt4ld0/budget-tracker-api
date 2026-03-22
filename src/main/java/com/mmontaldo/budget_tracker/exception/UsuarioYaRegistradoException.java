package com.mmontaldo.budget_tracker.exception;

public class UsuarioYaRegistradoException extends RuntimeException {

    public UsuarioYaRegistradoException(String message) {
        super(message);
    }
    
    public UsuarioYaRegistradoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UsuarioYaRegistradoException(Throwable cause) {
        super(cause);
    }
    
    public UsuarioYaRegistradoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
