package com.maurooyhanart.surveyq.backend.questionresponse.type.item;

import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatedItemRequest {
    @NotNull(message = "Question Item ID cannot be null")
    private Long questionItemId;
    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    /**
     * Constructs a RatedItem. Does not set the {@code RatedItemsQuestionResponse questionResponse} field
     * @param questionItem
     * @return
     */
    public RatedItem toRatedItem(QuestionItem questionItem) {
        RatedItem ratedItem = new RatedItem();
        ratedItem.setRating(getRating());
        ratedItem.setQuestionItem(questionItem);
        return ratedItem;
    }
}

