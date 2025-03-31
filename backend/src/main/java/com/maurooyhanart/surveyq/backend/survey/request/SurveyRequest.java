package com.maurooyhanart.surveyq.backend.survey.request;

import com.maurooyhanart.surveyq.backend.survey.Survey;
import com.maurooyhanart.surveyq.backend.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyRequest {
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Purpose cannot be null")
    private String purpose;
    @NotNull(message = "Published cannot be null")
    private Boolean published;

    public Survey toSurvey(User owner) {
        Survey survey = new Survey();
        survey.setName(getName());
        survey.setOwner(owner);
        survey.setPurpose(getPurpose());
        if ((getPublished() != null)) {
            survey.setPublished(getPublished());
        } else {
            survey.setPublished(false);
        }
        return survey;
    }
}
