package com.maurooyhanart.surveyq.backend.questionresponse.type.item;

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
public abstract class RatedItemsQuestionResponse extends QuestionResponse {

    @OneToMany(mappedBy = "questionResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatedItem> ratedItems;
}
