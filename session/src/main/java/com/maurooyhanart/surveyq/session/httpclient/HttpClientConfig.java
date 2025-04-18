package com.maurooyhanart.surveyq.session.httpclient;

import com.maurooyhanart.surveyq.shared.HttpClient;
import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpLogger httpLogger(@Value("${logging.url}") String loggingUrl,
                                 @Value("${api.key}") String apiKey,
                                 RestTemplate restTemplate) {
        return new HttpLogger(new HttpClient(
                loggingUrl + "/log", apiKey, restTemplate
        ));
    }

    @Bean
    public HttpClient backendClient(
            @Value("${backend.url}") String backendUrl,
            @Value("${api.key}") String apiKey,
            RestTemplate restTemplate // Inject the RestTemplate bean
    ) {
        return new HttpClient(backendUrl, apiKey, restTemplate);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}