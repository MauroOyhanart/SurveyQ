package com.maurooyhanart.surveyq.backend.question.type.poll;

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
@DiscriminatorValue("POLL")
public class PollQuestion extends ItemizedQuestion<TextQuestionItem> {

}
