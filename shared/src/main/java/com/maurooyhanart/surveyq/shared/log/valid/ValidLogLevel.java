package com.maurooyhanart.surveyq.shared.log.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = LogLevelValidator.class)
public @interface ValidLogLevel {
    String message() default "Invalid log level";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}