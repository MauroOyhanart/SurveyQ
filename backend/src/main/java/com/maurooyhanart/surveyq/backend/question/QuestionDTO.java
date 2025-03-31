package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestion;
import com.maurooyhanart.surveyq.backend.question.type.item.TextQuestionItem;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestion;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestion;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class QuestionDTO {
    private Long id;
    private Long surveyId;
    private String questionText;
    private String questionType;
    private Integer order;

    private List<TextQuestionItem> items;

    /**
     * Map to a DTO. Does not set {@code surveyId}
     * @param question
     * @return a QuestionDTO object without the {@code surveyId} field
     */
    public static QuestionDTO toQuestionDto(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setSurveyId(question.getSurveyId());
        dto.setOrder(question.getQuestionOrder());
        if (question instanceof PollQuestion) {
            dto.setQuestionType("POLL");
            dto.setItems(((PollQuestion) question).getQuestionItems());
            if (dto.getItems() != null)
                dto.getItems().forEach(item -> {
                    item.setQuestionId(question.getId());
                });
        } else if (question instanceof MultipleChoiceQuestion) {
            dto.setQuestionType("MULTIPLE_CHOICE");
            dto.setItems(((MultipleChoiceQuestion) question).getQuestionItems());
            if (dto.getItems() != null)
                dto.getItems().forEach(item -> {
                    item.setQuestionId(question.getId());
                });
        } else if (question instanceof RatingQuestion) {
            dto.setQuestionType("RATING");
            dto.setItems(((RatingQuestion) question).getQuestionItems());
            if (dto.getItems() != null)
                dto.getItems().forEach(item -> {
                    item.setQuestionId(question.getId());
                });
        } else if (question instanceof FreeFormQuestion) {
            dto.setQuestionType("FREE_FORM");
        } else dto.setQuestionType("Unknown");
        return dto;
    }
}
