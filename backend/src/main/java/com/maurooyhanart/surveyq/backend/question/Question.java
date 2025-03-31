package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestion;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestion;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestion;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "survey_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_survey_id"))
    @Column(name = "survey_id")
    private Long surveyId;

    @Column
    private String questionText;

    @Column
    private Integer questionOrder;

    public Question(Long id, Long surveyId, String questionText, Integer questionOrder) {
        this.id = id;
        this.surveyId = surveyId;
        this.questionText = questionText;
        this.questionOrder = questionOrder;
    }

    public static List<? extends QuestionItem> getParticularItems(Question question) {
        if (question instanceof PollQuestion) {
            return ((PollQuestion) question).getQuestionItems();
        } else if (question instanceof MultipleChoiceQuestion) {
            return ((MultipleChoiceQuestion) question).getQuestionItems();
        } else if (question instanceof RatingQuestion) {
            return ((RatingQuestion) question).getQuestionItems();
        } else if (question instanceof FreeFormQuestion) {
            return new ArrayList<>();
        } else return new ArrayList<>();
    }
}