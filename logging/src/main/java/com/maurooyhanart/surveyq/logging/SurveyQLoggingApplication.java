package com.maurooyhanart.surveyq.logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.maurooyhanart.surveyq"})
public class SurveyQLoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyQLoggingApplication.class);
    }
}
