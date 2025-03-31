package com.maurooyhanart.surveyq.backend.survey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyDTO {
    private Long id;
    private String name;
    private Long ownerId;
    private String purpose;

    public static SurveyDTO toSurveyDto(Survey survey) {
        SurveyDTO dto = new SurveyDTO();
        dto.setId(survey.getId());
        dto.setName(survey.getName());
        dto.setOwnerId(survey.getOwner().getId());
        dto.setPurpose(survey.getPurpose());
        return dto;
    }
}
