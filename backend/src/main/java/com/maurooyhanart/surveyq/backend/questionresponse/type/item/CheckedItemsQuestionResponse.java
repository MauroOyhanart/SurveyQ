package com.maurooyhanart.surveyq.backend.questionresponse.type.item;

import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
public abstract class CheckedItemsQuestionResponse extends QuestionResponse {
    @ManyToMany
    @JoinTable(
            name = "checked_items",
            joinColumns = @JoinColumn(name = "response_id"),
            inverseJoinColumns = @JoinColumn(name = "question_item_id")
    )
    private List<QuestionItem> checkedItems;
}