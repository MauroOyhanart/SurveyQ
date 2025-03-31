package com.maurooyhanart.surveyq.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.maurooyhanart.surveyq"})
public class SurveyQSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyQSessionApplication.class, args);
    }
}
