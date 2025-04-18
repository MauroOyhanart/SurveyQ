package com.maurooyhanart.surveyq.backend.question.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionDeleteRequest {
    @NotNull(message = "Question ID cannot be null")
    private Long questionId;
}
