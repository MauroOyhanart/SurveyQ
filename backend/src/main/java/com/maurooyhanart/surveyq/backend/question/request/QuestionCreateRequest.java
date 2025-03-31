package com.maurooyhanart.surveyq.backend.question.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "questionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultipleChoiceQuestionCreateRequest.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = PollQuestionCreateRequest.class, name = "POLL"),
        @JsonSubTypes.Type(value = RatingQuestionCreateRequest.class, name = "RATING"),
        @JsonSubTypes.Type(value = FreeFormQuestionCreateRequest.class, name = "FREE_FORM")
})
public abstract class QuestionCreateRequest {
    @NotNull(message = "Survey ID cannot be null")
    private Long surveyId;
    @NotNull(message = "Question text cannot be null")
    private String questionText;

    public abstract Question toQuestion(Survey survey);

    public static void validateRequest(QuestionCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("Request is null");
        if (request.getQuestionText() == null) throw new IllegalArgumentException("Request question text is null");

        if (request instanceof PollQuestionCreateRequest) {
            if (((PollQuestionCreateRequest) request).getTextItems() == null)
                throw new IllegalArgumentException("Request text items are null");
        } else if (request instanceof MultipleChoiceQuestionCreateRequest) {
            if (((MultipleChoiceQuestionCreateRequest) request).getTextItems() == null)
                throw new IllegalArgumentException("Request text items are null");
        } else if (request instanceof RatingQuestionCreateRequest) {
            if (((RatingQuestionCreateRequest) request).getTextItems() == null)
                throw new IllegalArgumentException("Request text items are null");
        }
    }
}
