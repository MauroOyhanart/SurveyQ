package com.maurooyhanart.surveyq.session.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private int status;
    private String message;
    private Map<String, String> errors;
    private long timestamp;

    public ErrorResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
        this.errors = errors;
    }
}
