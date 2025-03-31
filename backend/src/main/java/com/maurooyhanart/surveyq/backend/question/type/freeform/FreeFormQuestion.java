package com.maurooyhanart.surveyq.backend.question.type.freeform;

import com.maurooyhanart.surveyq.backend.question.Question;
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
public class FreeFormQuestion extends Question {

}
