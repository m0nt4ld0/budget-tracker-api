package com.mmontaldo.budget_tracker.exception;

public class RequestBodyInvalidException extends RuntimeException {
    public RequestBodyInvalidException(String message) {
        super(message);
    }
}