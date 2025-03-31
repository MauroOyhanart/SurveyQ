package com.maurooyhanart.surveyq.backend.survey;

import com.maurooyhanart.surveyq.backend.security.SecurityUtils;
import com.maurooyhanart.surveyq.backend.survey.request.SurveyRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class SurveyController {
    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/public/surveys")
    public ResponseEntity<List<SurveyDTO>> getAll() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/public/home")
    public ResponseEntity<String> getHome() {
        return ResponseEntity.ok("Hello Hola!!");
    }

    @PostMapping("/api/survey")
    public ResponseEntity<SurveyDTO> createSurvey(@Valid @RequestBody SurveyRequest surveyRequest) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();

        return ResponseEntity.ok(surveyService.createSurvey(surveyRequest, userEmail));
    }
}
