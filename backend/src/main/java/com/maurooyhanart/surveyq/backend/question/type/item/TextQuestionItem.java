package com.maurooyhanart.surveyq.backend.question.type.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TextQuestionItem extends QuestionItem {
    @Column(name = "question_item_text")
    private String text;

    public TextQuestionItem(String text) {
        this.text = text;
    }
}
