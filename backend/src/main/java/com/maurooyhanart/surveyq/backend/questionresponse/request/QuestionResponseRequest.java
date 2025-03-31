package com.maurooyhanart.surveyq.backend.questionresponse.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.freeform.FreeFormQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItem;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItemRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice.MultipleChoiceQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.poll.PollQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.rating.RatingQuestionResponseRequest;
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
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "questionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultipleChoiceQuestionResponseRequest.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = PollQuestionResponseRequest.class, name = "POLL"),
        @JsonSubTypes.Type(value = RatingQuestionResponseRequest.class, name = "RATING"),
        @JsonSubTypes.Type(value = FreeFormQuestionResponseRequest.class, name = "FREE_FORM")
})
public abstract class QuestionResponseRequest {
    @NotNull(message = "Question ID is required")
    private Long questionId;
    private Long surveyResponseId;
    /**
     * Constructs a response out of the response request.
     *
     * <p>Asks for a callback function {@code cbChecked} to retrieve the question items from the database.</p>
     * <p>Asks for a callback function {@code cbRated} to store the rated items into the database:</p>
     * <ul>
     * <li>{@code cbRated} must also set the questionItem of each checkedItem</li>
     * <li>{@code cbRated} does not set the response field of the RatedItem objects. The response is set inside this method.</li>
     * <li>{@code cbRated} must return the corresponding {@code List<CheckedItem>}</li>
     * </ul>
     * @param question the question object associated to this response
     * @param cbChecked the first callback function (described above)
     * @param cbRated the second callback function (described above)
     * @return a {@code QuestionResponse} object
     */
    public abstract QuestionResponse toResponse(Question question, Function<List<Long>, List<QuestionItem>> cbChecked,
                                                Function<List<RatedItemRequest>, List<RatedItem>> cbRated, SurveyResponse surveyResponse);
}
