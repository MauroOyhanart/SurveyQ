# Shared module

Code made available for other modules to use.

The most important part: a http client that supports (only post for now) for other modules. This client is prepared to talk to http services, primarily intended for the logging service.

Whenever the backend service needs to log to the logging service, it uses the HttpLogger, which in turns uses the HttpClient:

```java
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
```

## Use of Threads

We leverage the ExecutorService class of the java threads API to make asynchronous calls to the HTTP server that will listen to our requests. 

This is done like this so that logging via http doesn't slow down the application.

## Use of lifecycle hooks in Spring and Java

The Spring IoC container will manage the lifecycle of our java objects; we can hook events via annotations (or other ways) to initialize objects, perform integrity checks, or close any resources attached to the objects prior to their destruction.

To mantain simplicity, we are not using spring features; we are using the API of the JVM (via the @PreDestroy annotation) to hook an event before the destruction of our executor.

In this case, we shutdown the executor when the application is about to finish, to release any resources attached to it.
