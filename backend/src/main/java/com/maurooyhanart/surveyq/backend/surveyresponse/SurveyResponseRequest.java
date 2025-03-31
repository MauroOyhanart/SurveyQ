package com.maurooyhanart.surveyq.backend.surveyresponse;

import com.maurooyhanart.surveyq.backend.questionresponse.request.QuestionResponseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SurveyResponseRequest {
    @NotNull(message = "Survey ID cannot be null")
    private Long surveyId;

    @NotNull(message = "Question responses cannot be null")
    private List<QuestionResponseRequest> questionResponses = new ArrayList<>();
}
