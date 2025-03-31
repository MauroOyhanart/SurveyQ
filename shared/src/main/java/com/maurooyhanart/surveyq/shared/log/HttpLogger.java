package com.maurooyhanart.surveyq.shared.log;

import com.maurooyhanart.surveyq.shared.HttpClient;
import jakarta.annotation.PreDestroy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Component to be used as a Spring bean. It needs a bean of type HttpClient.
 */

public class HttpLogger {
    private final HttpClient loggingHttpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public HttpLogger(HttpClient loggingHttpClient) {
        this.loggingHttpClient = loggingHttpClient;
    }

    public void httpLog(String module, String message, String logLevel) {
        executor.submit(() -> {
            try {
                loggingHttpClient.doPostJsonApiKeyRequest(
                        new LogRequest(module, LogLevel.valueOf(logLevel), message)
                );
            } catch (Exception e) {
                System.err.println("Failed to log: " + e.getMessage());
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}
