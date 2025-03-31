package com.maurooyhanart.surveyq.shared.log;

import com.maurooyhanart.surveyq.shared.log.valid.ValidLogLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class LogRequest {
    @NotNull
    private String module;
    @NotNull
    @ValidLogLevel
    private LogLevel level;
    @NotNull
    private String message;

    public LogRequest(String module, LogLevel level, String message) {
        this.module = module;
        this.level = level;
        this.message = message;
    }

    public Log toLog() {
        Log log = new Log();
        log.setModule(getModule());
        log.setTimestamp(LocalDateTime.now());
        log.setLevel(getLevel());
        log.setMessage(getMessage());
        return log;
    }
}
