package com.mmontaldo.budget_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

import java.time.OffsetDateTime;

record ErrorResponse(
        int status,
        String message,
        OffsetDateTime timestamp) {
}

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {

        ErrorResponse error = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            OffsetDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                OffsetDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                OffsetDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            CategoriaNotFoundException.class,
            GastoFechaFuturaException.class,
            GastoImporteNegativoException.class,
            RequestBodyInvalidException.class,
            FechaInvalidaException.class,
            DatabaseConnectionException.class
    })

    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException ex) {
        HttpStatus status;
        if (ex instanceof CategoriaNotFoundException || ex instanceof GastoFechaFuturaException ||
                ex instanceof GastoImporteNegativoException || ex instanceof RequestBodyInvalidException ||
                ex instanceof FechaInvalidaException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof DatabaseConnectionException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ErrorResponse error = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                OffsetDateTime.now());
        return new ResponseEntity<>(error, status);
    }

}
