package com.mmontaldo.budget_tracker.exception;

public class EmailYaRegistradoException extends RuntimeException {

    public EmailYaRegistradoException(String message) {
        super(message);
    }
    
    public EmailYaRegistradoException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EmailYaRegistradoException(Throwable cause) {
        super(cause);
    }
    
    public EmailYaRegistradoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
