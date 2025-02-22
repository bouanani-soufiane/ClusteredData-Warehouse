package com.progressoft.FxDealsWarehouse.controller;

import com.progressoft.FxDealsWarehouse.exception.CurrencyNotAvailableException;
import com.progressoft.FxDealsWarehouse.exception.ErrorResponse;
import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    public static final String VALIDATION_FAILED_MESSAGE = "Invalid input. Please try again.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions ( final MethodArgumentNotValidException ex, WebRequest request ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(( error ) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                VALIDATION_FAILED_MESSAGE,
                request.getDescription(false),
                errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotAvailableException ( CurrencyNotAvailableException ex, WebRequest request ) {
        log.error("Currency not available: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName(),
                request.getDescription(false),
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCurrencyException ( InvalidCurrencyException ex, WebRequest request ) {
        log.error("Currency validation error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName(),
                request.getDescription(false),
                ex.getMessage()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDealId ( RequestAlreadyExistException ex, WebRequest request ) {
        log.error("Duplicate deal ID error", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName(),
                request.getDescription(false),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions ( Exception ex, WebRequest request ) {
        log.error("Unhandled exception occurred", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName(),
                request.getDescription(false),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
