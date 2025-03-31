package com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice;

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
public class MultipleChoiceQuestionResponseRequest extends QuestionResponseRequest {
    @NotNull(message = "Checked items list cannot be null")
    private List<Long> checkedItems;

    @Override
    public QuestionResponse toResponse(Question question, Function<List<Long>, List<QuestionItem>> cbChecked,
                                       Function<List<RatedItemRequest>, List<RatedItem>> cbRated, SurveyResponse surveyResponse) {
        MultipleChoiceQuestionResponse response = new MultipleChoiceQuestionResponse();
        response.setQuestion(question);
        response.setTimestamp(null);

        response.setCheckedItems(cbChecked.apply(getCheckedItems()));
        response.setSurveyResponse(surveyResponse);

        return response;
    }
}
