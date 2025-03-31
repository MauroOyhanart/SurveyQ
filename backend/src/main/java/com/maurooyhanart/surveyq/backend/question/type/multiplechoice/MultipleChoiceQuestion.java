package com.maurooyhanart.surveyq.backend.question.type.multiplechoice;

import com.maurooyhanart.surveyq.backend.question.type.item.ItemizedQuestion;
import com.maurooyhanart.surveyq.backend.question.type.item.TextQuestionItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuestion extends ItemizedQuestion<TextQuestionItem> {
    public MultipleChoiceQuestion(Long id, Long surveyId, String questionText, Integer questionOrder, List<TextQuestionItem> questionItems) {
        super(id, surveyId, questionText, questionOrder, questionItems);
    }
}
