package com.maurooyhanart.surveyq.backend.question.type.item;

import com.maurooyhanart.surveyq.backend.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Base class for a question item
 *
 * <p><i>Question: what is your favorite movie amongst these? </i></p>
 * <ul>
 *     <li><i>question item 1: superman</i></li>
 *     <li><i>question item 2: batman</i></li>
 * </ul>
 *
 * Question items are ordered and they belong to a particular {@link Question}
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class QuestionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "\"order\"")
    private Integer order;

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof QuestionItem)) return false;
        QuestionItem otherItem = (QuestionItem) other;
        return Objects.equals(getId(), otherItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
