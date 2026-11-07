package com.dogukan.takehometwo.exception;

import com.dogukan.takehometwo.dto.request.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFound.class)
    public ResponseEntity<ApiResponse> handleAccountNotFound(AccountNotFound ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return error(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(SameAccountException.class)
    public ResponseEntity<ApiResponse> handleSameAccount (SameAccountException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation (MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return error(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDuplicate(DataIntegrityViolationException ex) {
        return error(HttpStatus.CONFLICT, "Account number or email already exists");
    }

    private ResponseEntity<ApiResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse(false, message));
    }

}
