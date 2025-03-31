package com.maurooyhanart.surveyq.backend.questionresponse.type.rating;

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
public class RatingQuestionResponseRequest extends QuestionResponseRequest {
    @NotNull(message = "Rated Items list cannot be null")
    private List<RatedItemRequest> ratedItems;

    @Override
    public QuestionResponse toResponse(Question question, Function<List<Long>, List<QuestionItem>> cbChecked,
                                       Function<List<RatedItemRequest>, List<RatedItem>> cbRated, SurveyResponse surveyResponse) {
        RatingQuestionResponse response = new RatingQuestionResponse();
        response.setQuestion(question);
        response.setTimestamp(null);
        response.setRatedItems(cbRated.apply(getRatedItems()));
        System.out.println("xd123");
        response.getRatedItems().forEach(item -> item.setQuestionResponse(response));
        response.setSurveyResponse(surveyResponse);
        return response;
    }
}
