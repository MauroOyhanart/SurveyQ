package com.maurooyhanart.surveyq.backend.questionresponse.type.freeform;

import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.request.QuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItem;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItemRequest;
import com.maurooyhanart.surveyq.backend.surveyresponse.SurveyResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

@NoArgsConstructor
@Getter
@Setter
public class FreeFormQuestionResponseRequest extends QuestionResponseRequest {
    @NotNull(message = "Response text cannot be null")
    private String textResponse; //TODO change to responseText

    /**
     * Does not use the {@code cb}
     * @param question the question object associated to this response
     * @param cbChecked the callback
     * @return
     */
    @Override
    public QuestionResponse toResponse(Question question, Function<List<Long>, List<QuestionItem>> cbChecked,
                                       Function<List<RatedItemRequest>, List<RatedItem>> cbRated, SurveyResponse surveyResponse) {
        FreeFormQuestionResponse response = new FreeFormQuestionResponse();
        response.setQuestion(question);
        response.setTimestamp(null);
        response.setTextResponse(getTextResponse());
        response.setSurveyResponse(surveyResponse);
        return response;
    }
}