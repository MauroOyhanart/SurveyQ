package com.maurooyhanart.surveyq.backend.application;

import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import com.maurooyhanart.surveyq.backend.security.UnauthenticatedUserException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("Illegal argument", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Illegal argument provided",
                map
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.BAD_REQUEST.value() + " Illegal Argument Exception-> " + map, "ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Runtime Exception", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Runtime Exception",
                map
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.INTERNAL_SERVER_ERROR.value() + " Runtime Exception -> " + map, "ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later",
                null
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.INTERNAL_SERVER_ERROR.value() + " Internal Server Error -> " + ex.getMessage(), "ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.BAD_REQUEST.value() + " Validation failed -> " + errors, "ERROR");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<ErrorResponse> handleUnauthenticatedUserException(UnauthenticatedUserException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Authentication reason", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "User is unauthenticated",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.UNAUTHORIZED.value() + " User is unauthenticated -> " + errors, "ERROR");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Entity", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "The requested entity was not found",
                errors
        );

        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.NOT_FOUND.value() + " The requested entity was not found -> " + errors, "ERROR");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Error", "Malformed or invalid JSON in the request body");
        errors.put("Message", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid JSON format.",
                errors
        );

        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.BAD_REQUEST.value() + " Invalid JSON format -> " + errors, "ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("Authorization reason", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "User is not authorized",
                errors
        );
        logger.error("{}: {} {} {}", errorResponse.getTimestamp(), errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getErrors());
        httpLogger.httpLog("backend", HttpStatus.UNAUTHORIZED.value() + " User is not authorized -> " + errors, "ERROR");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}
