package com.maurooyhanart.surveyq.backend.questionresponse;

import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.QuestionRepository;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItemRepository;
import com.maurooyhanart.surveyq.backend.questionresponse.request.QuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.CheckedItemsQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItem;
import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItemRequest;
import com.maurooyhanart.surveyq.backend.security.UnauthenticatedUserException;
import com.maurooyhanart.surveyq.backend.survey.SurveyRepository;
import com.maurooyhanart.surveyq.backend.surveyresponse.SurveyResponse;
import com.maurooyhanart.surveyq.backend.surveyresponse.SurveyResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionResponseService {
    private final Logger logger = LoggerFactory.getLogger(QuestionResponseService.class);

    private final QuestionResponseRepository questionResponseRepository;
    private final QuestionRepository questionRepository;
    private final QuestionItemRepository questionItemRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyRepository surveyRepository;

    @Autowired
    public QuestionResponseService(QuestionResponseRepository questionResponseRepository, QuestionRepository questionRepository, QuestionItemRepository questionItemRepository, SurveyResponseRepository surveyResponseRepository, SurveyRepository surveyRepository) {
        this.questionResponseRepository = questionResponseRepository;
        this.questionRepository = questionRepository;
        this.questionItemRepository = questionItemRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.surveyRepository = surveyRepository;
    }

    /**
     * @param userEmail the authenticated user
     * @return all question responses of the indicated survey, which must be owned by the authenticated user.
     */
    @Transactional
    public List<QuestionResponseDTO> getAllResponses(Long surveyId, String userEmail) {
        if (surveyRepository.findById(surveyId).isEmpty())
            throw new EntityNotFoundException("Survey not found for ID: " + surveyId);

        //get all surveyResponses for surveys of the authenticated user
        Set<SurveyResponse> surveyResponses = surveyResponseRepository.findAllBySurveyId(surveyId);
        surveyResponses = surveyResponses.stream().filter(surveyResponse ->
            surveyResponse.getSurvey().getOwner().getEmail().equals(userEmail)
        ).collect(Collectors.toSet());

        //get the question responses of those surveyResponses
        Set<QuestionResponse> responses = questionResponseRepository.findAllBySurveyResponseIn(surveyResponses);

        return processAddMissingItems(responses);
    }

    public QuestionResponseDTO createResponse(@RequestBody QuestionResponseRequest questionResponseRequest, String userEmail) {
        Question question = questionRepository.findById(questionResponseRequest.getQuestionId()).orElseThrow(() ->
            new EntityNotFoundException("Question not found for ID: " + questionResponseRequest.getQuestionId())
        );
        SurveyResponse surveyResponse = surveyResponseRepository.findById(questionResponseRequest.getSurveyResponseId()).orElseThrow(() ->
            new EntityNotFoundException("SurveyResponse not found for ID: " + questionResponseRequest.getSurveyResponseId())
        );
        if (!surveyResponse.getUser().getEmail().equalsIgnoreCase(userEmail))
            throw new UnauthenticatedUserException("The survey response to modify belongs to another user.");

        Function<List<Long>, List<QuestionItem>> cbChecked = this::customLogicChecked;
        Function<List<RatedItemRequest>, List<RatedItem>> cbRated = this::customLogicRated;
        QuestionResponse questionResponse = questionResponseRepository.save(questionResponseRequest.toResponse(question, cbChecked, cbRated, surveyResponse));

        return QuestionResponseDTO.toResponseDto(questionResponse, processGetUncheckedItems(questionResponse));
    }

    public List<QuestionItem> customLogicChecked(List<Long> checkedItemIds) {
        if (checkedItemIds == null) return null;
        List<QuestionItem> checkedItems = questionItemRepository.findAllById(checkedItemIds);
        return checkedItems;
    }

    /**
     * Returns a list of RatedItems.
     * Does not set the {@code response} field of each RatedItem object.
     * @param ratedItems
     * @return
     */
    public List<RatedItem> customLogicRated(List<RatedItemRequest> ratedItems) {
        if (ratedItems == null) return null;
        List<RatedItem> ratedItemEntities = ratedItems.stream()
                .map(itemReq -> {

                    QuestionItem questionItem = questionItemRepository.findById(itemReq.getQuestionItemId()).orElseThrow(() -> {
                        String errorText = "QuestionItem not found for ID: " + itemReq.getQuestionItemId();
                        logger.error(errorText);
                        return new IllegalArgumentException(errorText);
                    });
                    RatedItem ratedItem = itemReq.toRatedItem(questionItem);

                    return ratedItem;
                }).toList();
        return ratedItemEntities;
    }

    /**
     * Builds the dtos and processes the missing items.
     * A checked item is an item marked as true.
     * Only checked items are stored, hence we're missing the ones marked as false. We're reconstructing that.
     * @param responses
     * @return
     */
    private List<QuestionResponseDTO> processAddMissingItems(Set<QuestionResponse> responses) {
        return responses.stream().map(questionResponse -> questionResponseDtoBuilder().apply(questionResponse)).toList();
    }

    private List<QuestionItem> processGetUncheckedItems(QuestionResponse questionResponse) {
        List<QuestionItem> checkedItems = processGetCheckedItems(questionResponse);
        if (checkedItems == null) return new ArrayList<>();

        List<QuestionItem> allItems = questionItemRepository.findByQuestionId(questionResponse.getQuestion().getId());

        allItems.removeIf(checkedItems::contains); //from all items, remove the checked ones.
        //allItems now contains the uncheckedItems
        return allItems;
    }

    private List<QuestionItem> processGetCheckedItems(QuestionResponse questionResponse) {
        if (questionResponse instanceof CheckedItemsQuestionResponse) {
            return ((CheckedItemsQuestionResponse) questionResponse).getCheckedItems();
        } else return null;
    }

    public Function<QuestionResponse, QuestionResponseDTO> questionResponseDtoBuilder() {
        return (questionResponse ->
            QuestionResponseDTO.toResponseDto(questionResponse, processGetUncheckedItems(questionResponse))
        );
    }
}
