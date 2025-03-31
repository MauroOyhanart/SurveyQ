package com.maurooyhanart.surveyq.backend.questionresponse;

import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.questionresponse.type.freeform.FreeFormQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.CheckedItemDTO;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItem;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItemDTO;
import com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice.MultipleChoiceQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.poll.PollQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.rating.RatingQuestionResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic, for any type of question this will be the DTO used.
 */
@NoArgsConstructor
@Getter
@Setter
public class QuestionResponseDTO {
    private Long id;

    private Long questionId;

    private LocalDateTime timestamp;

    //loose oop
    private List<CheckedItemDTO> checkedItems;
    //loose oop
    private List<RatedItemDTO> ratedItems;
    //loose oop
    private String textResponse;

    /**
     *
     * @param questionResponse the response to construct the DTO from
     * @param uncheckedItems the list of unchecked items that may be merged with this DTO. Must not be null
     * @return a DTO
     * @throws IllegalArgumentException if {@code uncheckedItems} is null
     */
    public static QuestionResponseDTO toResponseDto(QuestionResponse questionResponse, List<QuestionItem> uncheckedItems) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(questionResponse.getId());
        dto.setQuestionId(questionResponse.getQuestion().getId());
        dto.setTimestamp(questionResponse.getTimestamp());

        if (uncheckedItems == null) throw new IllegalArgumentException("uncheckedItems cannot be null");

        List<CheckedItemDTO> uncheckedItemsDtos = uncheckedItems.stream()
                .map(uncheckedItem -> new CheckedItemDTO(questionResponse.getId(), uncheckedItem.getId(), uncheckedItem.getOrder(), false)).toList();

        if (questionResponse instanceof MultipleChoiceQuestionResponse) {
            List<QuestionItem> checkedItemsLocal = ((MultipleChoiceQuestionResponse) questionResponse).getCheckedItems();
            List<CheckedItemDTO> checkedItemsDtos = checkedItemsLocal.stream()
                    .map(checkedItem -> new CheckedItemDTO(questionResponse.getId(), checkedItem.getId(), checkedItem.getOrder(), true)).collect(Collectors.toList());
            dto.setCheckedItems(checkedItemsDtos);

        } else if (questionResponse instanceof PollQuestionResponse) {
            List<QuestionItem> checkedItemsLocal = ((PollQuestionResponse) questionResponse).getCheckedItems();
            List<CheckedItemDTO> checkedItemsDtos = checkedItemsLocal.stream()
                    .map(checkedItem -> new CheckedItemDTO(questionResponse.getId(), checkedItem.getId(), checkedItem.getOrder(), true)).collect(Collectors.toList());
            dto.setCheckedItems(checkedItemsDtos);

        } else if (questionResponse instanceof RatingQuestionResponse) {
            List<RatedItem> ratedItemsLocal = ((RatingQuestionResponse) questionResponse).getRatedItems();
            List<RatedItemDTO> ratedItemDtos = ratedItemsLocal.stream().map(RatedItemDTO::toRatedItemDto).toList(); //immodifiable
            dto.setRatedItems(ratedItemDtos);
        } else if (questionResponse instanceof FreeFormQuestionResponse) {
            dto.setTextResponse(((FreeFormQuestionResponse) questionResponse).getTextResponse());
        }

        List<CheckedItemDTO> allCheckedOrUncheckedItemDtos = dto.getCheckedItems();
        if (allCheckedOrUncheckedItemDtos != null) {
            allCheckedOrUncheckedItemDtos.addAll(uncheckedItemsDtos);
            allCheckedOrUncheckedItemDtos.sort(Comparator.comparing(CheckedItemDTO::getOrder));
        }
        dto.setCheckedItems(allCheckedOrUncheckedItemDtos);
        return dto;
    }
}
