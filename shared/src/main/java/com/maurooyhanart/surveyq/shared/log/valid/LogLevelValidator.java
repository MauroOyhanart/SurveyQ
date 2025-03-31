package com.maurooyhanart.surveyq.shared.log.valid;

import com.maurooyhanart.surveyq.shared.log.LogLevel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.EnumUtils;

public class LogLevelValidator implements ConstraintValidator<ValidLogLevel, LogLevel> {

    @Override
    public boolean isValid(LogLevel logLevel, ConstraintValidatorContext context) {
        if (logLevel == null) {
            return true;
        }
        return EnumUtils.isValidEnum(LogLevel.class, logLevel.name());
    }
}
