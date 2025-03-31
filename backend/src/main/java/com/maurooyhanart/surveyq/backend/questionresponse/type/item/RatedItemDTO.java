package com.maurooyhanart.surveyq.backend.questionresponse.type.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RatedItemDTO {
    private Long id;
    private Long questionResponseId;
    private Long questionItemId;
    private Integer rating;

    public static RatedItemDTO toRatedItemDto(RatedItem ratedItem) {
        RatedItemDTO dto = new RatedItemDTO();
        dto.setId(ratedItem.getId());
        dto.setQuestionResponseId(ratedItem.getQuestionResponse().getId());
        dto.setQuestionItemId(ratedItem.getQuestionItem().getId());
        dto.setRating(ratedItem.getRating());

        return dto;
    }
}
