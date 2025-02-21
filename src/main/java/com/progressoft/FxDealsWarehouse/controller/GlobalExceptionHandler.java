package com.progressoft.FxDealsWarehouse.controller;

import com.progressoft.FxDealsWarehouse.exception.InvalidCurrencyException;
import com.progressoft.FxDealsWarehouse.exception.RequestAlreadyExistException;
import com.progressoft.FxDealsWarehouse.exception.ErrorResponse;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCurrencyException(InvalidCurrencyException ex, WebRequest request) {
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
    public ResponseEntity<ErrorResponse> handleDuplicateDealId( RequestAlreadyExistException ex, WebRequest request) {
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
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
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
