package com.maurooyhanart.surveyq.backend.surveyresponse;

import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

@NoArgsConstructor
@Getter
@Setter
public class SurveyResponseDTO {
    private Long id;
    private Long surveyId;
    private Long userId;
    private List<QuestionResponseDTO> questionResponses;

    public static SurveyResponseDTO toSurveyResponseDto(SurveyResponse response, Function<QuestionResponse, QuestionResponseDTO> questionDtoBuilder) {
        SurveyResponseDTO dto = new SurveyResponseDTO();
        dto.setId(response.getId());
        dto.setSurveyId(response.getSurvey().getId());
        dto.setUserId(response.getUser().getId());
        dto.setQuestionResponses(response.getQuestionResponses().stream()
                .map(questionDtoBuilder).toList());
        return dto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id [").append(id).append("], surveyId [").append(surveyId).append("]");
        sb.append(", userId [").append(userId).append("]").append(", with List questionResponses");
        return sb.toString();
    }
}
