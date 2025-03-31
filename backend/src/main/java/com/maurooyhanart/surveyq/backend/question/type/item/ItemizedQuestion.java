package com.maurooyhanart.surveyq.backend.question.type.item;

import com.maurooyhanart.surveyq.backend.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class ItemizedQuestion<T extends QuestionItem> extends Question {
    /*
        private Long id;

        private Long surveyId;

        private String questionText;

        private Integer questionOrder;
     */

    @OneToMany(targetEntity = QuestionItem.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private List<T> questionItems;

    public ItemizedQuestion(Long id, Long surveyId, String questionText, Integer questionOrder, List<T> questionItems) {
        super(id, surveyId, questionText, questionOrder);
        this.questionItems = questionItems;
    }
}
