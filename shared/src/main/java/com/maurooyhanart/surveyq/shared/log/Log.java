package com.maurooyhanart.surveyq.shared.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Log {
    private String module;
    private LocalDateTime timestamp;
    private LogLevel level;
    private String message;

    @Override
    public String toString() {
        return String.format("[%s] %s - %s: %s", timestamp, module, level, message);
    }
}
