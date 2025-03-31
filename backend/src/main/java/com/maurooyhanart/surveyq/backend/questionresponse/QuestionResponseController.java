package com.maurooyhanart.surveyq.backend.questionresponse;

import com.maurooyhanart.surveyq.backend.questionresponse.request.QuestionResponseRequest;
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

import java.util.List;

@RestController
@Validated
public class QuestionResponseController {
    private final QuestionResponseService questionResponseService;

    @Autowired
    public QuestionResponseController(QuestionResponseService questionResponseService) {
        this.questionResponseService = questionResponseService;
    }

    /**
     * Returns a list of all responses of the indicated survey. The authenticated user must be the owner.
     * @return
     */
    @GetMapping("/api/responses")
    public ResponseEntity<List<QuestionResponseDTO>> getAllResponses(@Valid @NotNull Long surveyId) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        return ResponseEntity.ok(questionResponseService.getAllResponses(surveyId, userEmail));
    }

    // Create a response
    @PostMapping("/api/response")
    public ResponseEntity<QuestionResponseDTO> createResponse(@Valid @RequestBody QuestionResponseRequest response) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        return ResponseEntity.ok(questionResponseService.createResponse(response, userEmail));
    }
}

