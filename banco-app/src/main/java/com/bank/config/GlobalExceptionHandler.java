package com.bank.config;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.application.dto.BankingDto.ApiResponse;
import com.bank.domain.exception.AccessDeniedException;
import com.bank.domain.exception.AccountOperationNotAllowedException;
import com.bank.domain.exception.DomainValidationException;
import com.bank.domain.exception.InsufficientFundsException;
import com.bank.domain.exception.InvalidLoanStateTransitionException;
import com.bank.domain.exception.InvalidTransferStateException;
import com.bank.domain.exception.ResourceNotFoundException;

/**
* CONFIG - GlobalExceptionHandler
*
* Traduce las excepciones de dominio y de la aplicación en respuestas HTTP.
* Forma parte de la capa de interfaz y mantiene la coherencia en el manejo de errores en todos los controladores.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error("Acceso denegado: " + ex.getMessage()));
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainValidation(DomainValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity.status(422)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(AccountOperationNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccountOperation(AccountOperationNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidLoanStateTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleLoanState(InvalidLoanStateTransitionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidTransferStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleTransferState(InvalidTransferStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("Validación fallida: " + errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("Error inesperado: " + ex.getMessage()));
    }
}
