package com.maurooyhanart.surveyq.backend.question.type.rating;

import com.maurooyhanart.surveyq.backend.question.type.item.ItemizedQuestion;
import com.maurooyhanart.surveyq.backend.question.type.item.TextQuestionItem;
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
public class RatingQuestion extends ItemizedQuestion<TextQuestionItem> {
    
}
