package com.joukou.fxexchangeservice.exception;

import com.joukou.fxexchangeservice.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleExternalServiceException(
            ExternalServiceException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode("FX_EXTERNAL_ERROR")
                .status(502)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                .errorCode("VALIDATION_ERROR")
                .status(400)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .message("Unexpected error occurred")
                .errorCode("INTERNAL_ERROR")
                .status(500)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }
}
