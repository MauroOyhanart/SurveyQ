package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.question.request.QuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/public/questions")
    public ResponseEntity<List<QuestionDTO>> getAll() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @PostMapping("/api/question")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        String userEmail = SecurityUtils.getAuthenticatedUserEmail();
        System.out.println("authenticated user email: " + userEmail);
        QuestionDTO createdQuestion = questionService.createQuestion(questionCreateRequest, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }
}
