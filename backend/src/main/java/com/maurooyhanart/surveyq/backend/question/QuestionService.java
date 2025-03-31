package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.application.PatternMatcher;
import com.maurooyhanart.surveyq.backend.question.request.QuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.security.UnauthenticatedUserException;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import com.maurooyhanart.surveyq.backend.survey.SurveyOwnershipChecker;
import com.maurooyhanart.surveyq.backend.survey.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
public class QuestionService {
    private final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final String EMAIL_PATTERN = "^(.+)@(\\S+)$";

    private final QuestionRepository questionRepository;
    private final SurveyOwnershipChecker surveyOwnershipChecker;
    private final SurveyRepository surveyRepository;


    public QuestionService(QuestionRepository questionRepository, SurveyRepository surveyRepository, SurveyOwnershipChecker surveyOwnershipChecker) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.surveyOwnershipChecker = surveyOwnershipChecker;
    }

    /**
     * Returns all questions in the repository.
     * @return
     */
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(QuestionDTO::toQuestionDto).toList();
    }

    /**
     * Creates a Question for the specified survey, which must belong to the authenticated user.
     * @param questionCreateRequest the json request
     * @param userEmail the authenticated user
     * @return the DTO associated with the created Question object
     */
    public QuestionDTO createQuestion(QuestionCreateRequest questionCreateRequest, String userEmail) {
        QuestionCreateRequest.validateRequest(questionCreateRequest);
        PatternMatcher.patternMatches(userEmail, EMAIL_PATTERN);

        Survey survey = surveyRepository.findById(questionCreateRequest.getSurveyId()).orElseThrow(() -> {
            String errorText = "Survey not found for ID: " + questionCreateRequest.getSurveyId();
            return new EntityNotFoundException(errorText);
        });
        if (!surveyOwnershipChecker.isOwner(survey.getId(), userEmail)) {
            throw new UnauthenticatedUserException("User is not the owner of this survey object.");
        }
        Set<Question> surveyQuestions = questionRepository.findAllBySurveyId(survey.getId());
        surveyQuestions.stream()
                .filter(question ->
                        question.getQuestionText().equalsIgnoreCase(questionCreateRequest.getQuestionText())
                ).findFirst().ifPresent(ign -> {
                    throw new IllegalArgumentException("A question with this question text already exists.");
                });
        Question question = questionCreateRequest.toQuestion(survey);
        Integer count = surveyQuestions.size();
        question.setQuestionOrder(count);

        question = questionRepository.save(question);

        if (question.getQuestionOrder() < 0)  throw new IllegalStateException("Question Order cannot be less than 0");

        Question finalQuestion = question;
        Question.getParticularItems(question).forEach(item -> item.setQuestionId(finalQuestion.getId()));

        return QuestionDTO.toQuestionDto(question);
    }




}

