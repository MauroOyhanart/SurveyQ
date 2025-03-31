package com.maurooyhanart.surveyq.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.maurooyhanart.surveyq"})
public class SurveyQBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyQBackendApplication.class, args);
    }
}
