package com.maurooyhanart.surveyq.backend.question.type.freeform;

import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.request.QuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FreeFormQuestionCreateRequest extends QuestionCreateRequest {
    @Override
    public Question toQuestion(Survey survey) {
        FreeFormQuestion question = new FreeFormQuestion();

        question.setSurveyId(survey.getId());
        question.setQuestionText(getQuestionText());

        return question;
    }
}
