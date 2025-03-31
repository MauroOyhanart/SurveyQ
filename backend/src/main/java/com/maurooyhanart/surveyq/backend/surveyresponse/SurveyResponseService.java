package com.maurooyhanart.surveyq.backend.surveyresponse;

import com.maurooyhanart.surveyq.backend.questionresponse.type.item.RatedItem;
import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import com.maurooyhanart.surveyq.backend.question.Question;
import com.maurooyhanart.surveyq.backend.question.QuestionRepository;
import com.maurooyhanart.surveyq.backend.question.type.item.QuestionItem;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponseRepository;
import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponseService;
import com.maurooyhanart.surveyq.backend.questionresponse.request.QuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.freeform.FreeFormQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.freeform.FreeFormQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice.MultipleChoiceQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.multiplechoice.MultipleChoiceQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.poll.PollQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.poll.PollQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.questionresponse.type.rating.RatingQuestionResponse;
import com.maurooyhanart.surveyq.backend.questionresponse.type.rating.RatingQuestionResponseRequest;
import com.maurooyhanart.surveyq.backend.security.UnauthenticatedUserException;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import com.maurooyhanart.surveyq.backend.survey.SurveyRepository;
import com.maurooyhanart.surveyq.backend.user.User;
import com.maurooyhanart.surveyq.backend.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveyResponseService {
    private final Logger logger = LoggerFactory.getLogger(SurveyResponseService.class);
    private final HttpLogger httpLogger;

    private final SurveyResponseRepository surveyResponseRepository;
    private final QuestionRepository questionRepository;
    private final QuestionResponseRepository questionResponseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionResponseService questionResponseService;
    private final UserRepository userRepository;

    @Autowired
    public SurveyResponseService(SurveyResponseRepository surveyResponseRepository, QuestionRepository questionRepository, QuestionResponseRepository questionResponseRepository, SurveyRepository surveyRepository, QuestionResponseService questionResponseService, UserRepository userRepository, HttpLogger httpLogger) {
        this.surveyResponseRepository = surveyResponseRepository;
        this.questionRepository = questionRepository;
        this.questionResponseRepository = questionResponseRepository;
        this.surveyRepository = surveyRepository;
        this.questionResponseService = questionResponseService;
        this.userRepository = userRepository;
        this.httpLogger = httpLogger;
    }

    @Transactional
    public Set<SurveyResponseDTO> getAllSurveyResponses(Long surveyId, String userEmail) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() ->
            new EntityNotFoundException("Survey not found for ID: " + surveyId)
        );
        if (!survey.getOwner().getEmail().equalsIgnoreCase(userEmail))
            throw new UnauthenticatedUserException("User is not the owner of this survey.");

        Set<SurveyResponse> responses = surveyResponseRepository.findAllBySurveyId(surveyId);
        Set<SurveyResponseDTO> dtos = responses.stream()
                .map(response ->
            SurveyResponseDTO.toSurveyResponseDto(response, questionResponseService.questionResponseDtoBuilder())
        ).collect(Collectors.toSet());
        log("Returned all survey responses for " + userEmail);
        return dtos;
    }

    @Transactional
    public Set<SurveyResponseDTO> getAllSurveyResponsesOfUser(String userEmail) {
        userRepository.findByEmail(userEmail).orElseThrow(() ->
                new EntityNotFoundException("User not found for ID: " + userEmail)
        );
        Set<SurveyResponse> surveyResponses = surveyResponseRepository.findAllByUser_Email(userEmail);
        Set<SurveyResponseDTO> dtos = surveyResponses.stream()
            .map(response ->
                    SurveyResponseDTO.toSurveyResponseDto(response, questionResponseService.questionResponseDtoBuilder())
            ).collect(Collectors.toSet());
        log("Returned all survey responses of " + userEmail);
        return dtos;
    }

    @Transactional
    public SurveyResponseDTO createSurveyResponse(SurveyResponseRequest surveyResponseRequest, String userEmail) {
        SurveyResponse surveyResponse = new SurveyResponse();
        Long surveyId = surveyResponseRequest.getSurveyId();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> {
            String errorText = "Survey not found for ID: " + surveyId;
            return new EntityNotFoundException(errorText);
        });
        if (survey.getOwner().getEmail().equalsIgnoreCase(userEmail))
            throw new UnauthenticatedUserException("User cannot respond its own survey");

        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
            new EntityNotFoundException("User not found for ID: " + userEmail)
        );

        surveyResponse.setSurvey(survey);
        surveyResponse.setUser(user);
        surveyResponse = surveyResponseRepository.save(surveyResponse);

        List<QuestionResponse> questionResponses = handleQuestions(surveyResponse, surveyResponseRequest);
        surveyResponse.setQuestionResponses(questionResponses);

        SurveyResponseDTO dto =  SurveyResponseDTO.toSurveyResponseDto(surveyResponse, questionResponseService.questionResponseDtoBuilder());
        log("Created survey response: " + dto + " for " + userEmail);
        return dto;
    }

    private List<QuestionResponse> handleQuestions(SurveyResponse surveyResponse, SurveyResponseRequest surveyResponseRequest) {
        List<QuestionResponseRequest> questionResponseRequests = surveyResponseRequest.getQuestionResponses();
        List<QuestionResponse> questionResponses = questionResponseRequests.stream()
                .map(questionResponseRequest -> {

                    Long questionId = questionResponseRequest.getQuestionId();
                    Question question = questionRepository.findById(questionId).orElseThrow(() -> {
                        String errorText = "Question not found for ID: " + questionId;
                        return new EntityNotFoundException(errorText);
                    });

                    if (questionResponseRequest instanceof MultipleChoiceQuestionResponseRequest) {
                        MultipleChoiceQuestionResponse questionResponse = new MultipleChoiceQuestionResponse();
                        questionResponse.setQuestion(question);
                        questionResponse.setSurveyResponse(surveyResponse);
                        List<QuestionItem> checkedItems = questionResponseService.customLogicChecked(((MultipleChoiceQuestionResponseRequest) questionResponseRequest).getCheckedItems());
                        questionResponse.setCheckedItems(checkedItems);
                        return questionResponseRepository.save(questionResponse);
                    } else if (questionResponseRequest instanceof PollQuestionResponseRequest) {
                        PollQuestionResponse questionResponse = new PollQuestionResponse();
                        questionResponse.setQuestion(question);
                        questionResponse.setSurveyResponse(surveyResponse);
                        if (((PollQuestionResponseRequest) questionResponseRequest).getCheckedItems() == null) {
                            throw new IllegalArgumentException("Checked items is null");
                        }
                        List<QuestionItem> checkedItems = questionResponseService.customLogicChecked(((PollQuestionResponseRequest) questionResponseRequest).getCheckedItems());
                        questionResponse.setCheckedItems(checkedItems);
                        return questionResponseRepository.save(questionResponse);
                    } else if (questionResponseRequest instanceof RatingQuestionResponseRequest) {
                        RatingQuestionResponse questionResponse = new RatingQuestionResponse();
                        questionResponse.setQuestion(question);
                        questionResponse.setSurveyResponse(surveyResponse);
                        List<RatedItem> ratedItems = questionResponseService.customLogicRated(((RatingQuestionResponseRequest) questionResponseRequest).getRatedItems()); //SEE questioNRes
                        ratedItems.forEach(item -> item.setQuestionResponse(questionResponse));
                        questionResponse.setRatedItems(ratedItems);
                        return questionResponseRepository.save(questionResponse);
                    } else if (questionResponseRequest instanceof FreeFormQuestionResponseRequest) {
                        FreeFormQuestionResponse questionResponse = new FreeFormQuestionResponse();
                        questionResponse.setQuestion(question);
                        questionResponse.setSurveyResponse(surveyResponse);
                        questionResponse.setTextResponse(((FreeFormQuestionResponseRequest) questionResponseRequest).getTextResponse());
                        return questionResponseRepository.save(questionResponse);
                    } else throw new IllegalArgumentException("Unexpected type of QuestionResponseRequest: " + questionResponseRequest.getClass());
                }).toList();
        questionResponseRepository.saveAll(questionResponses);
        return questionResponses;
    }

    private void log(Object o) {
        logger.info(o.toString());
        httpLogger.httpLog("backend", o.toString(), "INFO");
    }
}
