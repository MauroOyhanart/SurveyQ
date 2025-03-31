package com.maurooyhanart.surveyq.session.application;

import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final HttpLogger httpLogger;

    @Autowired
    public GlobalExceptionHandler(HttpLogger httpLogger) {
        System.out.println("GlobalExceptionHandler is being loaded");
        this.httpLogger = httpLogger;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Illegal argument", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Illegal argument provided",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("session", HttpStatus.BAD_REQUEST.value() + " Illegal Argument Exception -> " + errors, "ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("session", HttpStatus.BAD_REQUEST.value() + " Validation failed -> " + errors, "ERROR");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Runtime Exception", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Runtime Exception",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("session", HttpStatus.INTERNAL_SERVER_ERROR.value() + "Runtime Exception -> " + errors, "ERROR");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Authentication Exception", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Exception",
                errors
        );
        logger.warn("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("session", HttpStatus.UNAUTHORIZED.value() + " Authentication Exception -> " + errors, "WARN");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("RestClient Exception", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "RestClient Exception",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("session", HttpStatus.INTERNAL_SERVER_ERROR.value() + " RestClient Exception -> " + errors, "ERROR");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}