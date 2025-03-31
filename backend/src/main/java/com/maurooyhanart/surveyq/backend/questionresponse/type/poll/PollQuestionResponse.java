package com.maurooyhanart.surveyq.backend.questionresponse.type.poll;

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
@DiscriminatorValue("POLL")
public class PollQuestionResponse extends CheckedItemsQuestionResponse {
}
