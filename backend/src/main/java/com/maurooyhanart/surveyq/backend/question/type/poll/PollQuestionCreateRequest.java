package com.maurooyhanart.surveyq.backend.question.type.poll;

import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.request.QuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.item.TextQuestionItem;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PollQuestionCreateRequest extends QuestionCreateRequest {
    @NotNull(message = "Text items list cannot be null")
    private List<TextQuestionItem> textItems;

    @Override
    public Question toQuestion(Survey survey) {
        PollQuestion question = new PollQuestion();

        question.setSurveyId(survey.getId());
        question.setQuestionText(getQuestionText());

        question.setQuestionItems(textItems);

        return question;
    }
}
