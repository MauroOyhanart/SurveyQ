package com.maurooyhanart.surveyq.backend.questionresponse.type.rating;

import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItemsQuestionResponse;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("RATING")
public class RatingQuestionResponse extends RatedItemsQuestionResponse {

}
