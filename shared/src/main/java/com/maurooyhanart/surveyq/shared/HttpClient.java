package com.maurooyhanart.surveyq.shared;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpClient {

    private final String url;
    private final String apiKey;
    private final RestTemplate restTemplate;

    public HttpClient(String url, String apiKey, RestTemplate restTemplate) {
        this.url = url;
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        System.out.println("Built We");
    }

    /**
     * Make a POST request with the specified headers and the payload {@code o}
     * @param headers http headers object
     * @param o the object for the HttpEntity of type T
     * @return the API response
     * @param <T> the type of the HttpEntity object to send
     */
    public <T> ResponseEntity<Void> doPostRequest(HttpHeaders headers, T o) {
        return doPost(url, o, headers);
    }

    /**
     * Make a POST request to the specified absolute URL with the specified headers and the payload {@code o}
     * @param theUrl absolute url to query
     * @param headers http headers object
     * @param o the object for the HttpEntity of type T
     * @return the API response
     * @param <T> the type of the HttpEntity object to send
     */
    public <T> ResponseEntity<Void> doPostRequest(String theUrl, HttpHeaders headers, T o) {
        return doPost(theUrl, o, headers);
    }

    /**
     * Make a POST request with the payload {@code o}
     * @param o the object for the HttpEntity of type T
     * @return the API response
     * @param <T> the type of the HttpEntity object to send
     */
    public <T> ResponseEntity<Void> doPostJsonApiKeyRequest(T o) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return doPost(url, o, headers);
    }

    private <T> ResponseEntity<Void> doPost(String theUrl, T o, HttpHeaders headers) {
        HttpEntity<T> request = new HttpEntity<>(o, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                theUrl,
                request,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("HTTP POST failed: " + response.getBody());
        }

        return response;
    }
}