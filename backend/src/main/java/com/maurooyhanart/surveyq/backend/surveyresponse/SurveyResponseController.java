package com.maurooyhanart.surveyq.backend.surveyresponse;

import com.maurooyhanart.surveyq.backend.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Validated
public class SurveyResponseController {
    private final SurveyResponseService surveyResponseService;

    @Autowired
    public SurveyResponseController(SurveyResponseService surveyResponseService) {
        this.surveyResponseService = surveyResponseService;
    }

    /**
     * Return all survey responses for the provided survey.
     * The authenticated user must be the owner of the survey.
     * @param surveyId the provided survey's id
     * @return all survey responses for the provided survey.
     */
    @GetMapping("/api/survey/responses")
    public ResponseEntity<Set<SurveyResponseDTO>> getAll(@Valid @NotNull Long surveyId) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        return ResponseEntity.ok(surveyResponseService.getAllSurveyResponses(surveyId, userEmail));
    }

    /**
     * Return all survey responses created by the authenticated user
     * @return survey responses
     */
    @GetMapping("/api/survey/responses/own")
    public ResponseEntity<Set<SurveyResponseDTO>> getAllOwn() {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        return ResponseEntity.ok(surveyResponseService.getAllSurveyResponsesOfUser(userEmail));
    }

    @PostMapping("/api/survey/response")
    public ResponseEntity<SurveyResponseDTO> createSurvey(@Valid @RequestBody SurveyResponseRequest surveyResponseRequest) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        return ResponseEntity.ok(surveyResponseService.createSurveyResponse(surveyResponseRequest, userEmail));
    }
}
