package com.maurooyhanart.surveyq.backend.questionresponse.type.item;

import lombok.Getter;
import lombok.Setter;

/**
 * This object is not stored as a checked item; rather, each checked item goes as an item in a list of checked items in a question response object.
 * This object exists then just as a representation of internal storage of the checked items  in a question response object or row.
 */
@Getter
@Setter
public class CheckedItemDTO {
    private Long responseId;
    private Long questionItemId;
    private Integer order;
    private Boolean checked;

    public CheckedItemDTO(Long responseId, Long questionItemId, Integer order, Boolean checked) {
        this.responseId = responseId;
        this.questionItemId = questionItemId;
        this.order = order;
        this.checked = checked;
    }
}
