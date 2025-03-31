package com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice;

import com.maurooyhanart.surveyq.backend.questionresponse.type.item.CheckedItemsQuestionResponse;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuestionResponse extends CheckedItemsQuestionResponse {

}
