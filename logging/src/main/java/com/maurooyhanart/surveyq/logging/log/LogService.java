package com.maurooyhanart.surveyq.logging.log;

import com.maurooyhanart.surveyq.shared.log.Log;
import com.maurooyhanart.surveyq.shared.log.LogRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@NoArgsConstructor
public class LogService {

    @Value("${log.directory:logs}")
    private String logDirectory;

    public void processLog(LogRequest logRequest) {
        Log log = logRequest.toLog();
        System.out.println(log);
        writeLogToFile(log);
    }

    private void writeLogToFile(Log log) {
        try {
            Files.createDirectories(Paths.get(logDirectory));

            String date = log.getTimestamp().toLocalDate().toString();
            String filename = String.format("%s/%s-%s.log", logDirectory, log.getModule(), date);

            String logEntry = log.toString() + System.lineSeparator();
            Files.write(Paths.get(filename), logEntry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }
}
