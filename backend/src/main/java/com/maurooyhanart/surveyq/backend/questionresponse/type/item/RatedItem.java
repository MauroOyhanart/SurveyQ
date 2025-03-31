package com.maurooyhanart.surveyq.backend.questionresponse.type.item;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "rated_item_response")
public class RatedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_response_id", nullable = false)
    private RatedItemsQuestionResponse questionResponse;

    @ManyToOne
    @JoinColumn(name = "question_item_id", nullable = false)
    private QuestionItem questionItem;

    @Column(nullable = false)
    private Integer rating;
}

