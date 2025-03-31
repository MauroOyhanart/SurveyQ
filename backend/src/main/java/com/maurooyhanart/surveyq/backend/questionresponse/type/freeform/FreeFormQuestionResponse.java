package com.maurooyhanart.surveyq.backend.questionresponse.type.freeform;

import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("FREE_FORM")
public class FreeFormQuestionResponse extends QuestionResponse {
    private String textResponse;
}
