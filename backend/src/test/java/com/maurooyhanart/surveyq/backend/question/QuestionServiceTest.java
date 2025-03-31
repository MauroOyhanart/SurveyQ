package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestion;
import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestion;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestion;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestion;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.security.UnauthenticatedUserException;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import com.maurooyhanart.surveyq.backend.survey.SurveyOwnershipChecker;
import com.maurooyhanart.surveyq.backend.survey.SurveyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.maurooyhanart.surveyq.backend.question.QuestionServiceTestUtilityMethods.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private SurveyOwnershipChecker surveyOwnershipChecker;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private QuestionService questionService;

    //----- getAllQuestions

    @Test
    void getAllQuestions_shouldReturnQuestionDTOList() {
        // Arrange
        FreeFormQuestion freeFormQuestion = new FreeFormQuestion();
        freeFormQuestion.setId(1L);
        freeFormQuestion.setSurveyId(101L);
        freeFormQuestion.setQuestionText("What is your name?");
        freeFormQuestion.setQuestionOrder(1);

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setId(2L);
        multipleChoiceQuestion.setSurveyId(101L);
        multipleChoiceQuestion.setQuestionText("Choose a color:");
        multipleChoiceQuestion.setQuestionOrder(2);

        when(questionRepository.findAll()).thenReturn(List.of(freeFormQuestion, multipleChoiceQuestion));

        // Act
        var result = questionService.getAllQuestions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("What is your name?", result.get(0).getQuestionText());
        assertEquals("Choose a color:", result.get(1).getQuestionText());
    }


    @Test
    void getAllQuestions_shouldReturnEmptyListWhenNoQuestions() {
        // Arrange
        when(questionRepository.findAll()).thenReturn(List.of());

        // Act
        var result = questionService.getAllQuestions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //----- createQuestion

    @Test
    void createMultipleChoiceQuestion_shouldCreateAndReturnQuestionDTO() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        MultipleChoiceQuestion savedQuestion = getAMultipleChoiceQuestionFromRequest(request);
        when(questionRepository.save(any(MultipleChoiceQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Pick your favorite fruit", result.getQuestionText());
        assertEquals(2, result.getItems().size());
    }

    @Test
    void createPollQuestion_shouldCreateAndReturnQuestionDTO() {
        // Arrange
        Survey survey = getASurvey();
        PollQuestionCreateRequest request = getAPollQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        PollQuestion savedQuestion = getAPollQuestionFromRequest(request);
        when(questionRepository.save(any(PollQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Pick your favorite dish", result.getQuestionText());
        assertEquals(2, result.getItems().size());
    }

    @Test
    void createFreeFormQuestion_shouldCreateAndReturnQuestionDTO() {
        // Arrange
        Survey survey = getASurvey();
        FreeFormQuestionCreateRequest request = getAFreeFormQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        FreeFormQuestion savedQuestion = getAFreeFormQuestionFromRequest(request);
        when(questionRepository.save(any(FreeFormQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Explain a topic", result.getQuestionText());
        assertNull(result.getItems());
    }

    @Test
    void createRatingQuestion_shouldCreateAndReturnQuestionDTO() {
        // Arrange
        Survey survey = getASurvey();
        RatingQuestionCreateRequest request = getARatingQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        RatingQuestion savedQuestion = getARatingQuestionFromRequest(request);
        when(questionRepository.save(any(RatingQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Rate these movies", result.getQuestionText());
        assertEquals(2, result.getItems().size());
    }

    //----- createQuestion -- failed

    @Test
    void createQuestionThatExists_shouldThrowIllegalArgumentException() {
        // Arrange
        Survey survey = new Survey();
        survey.setId(101L);

        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);

        MultipleChoiceQuestion savedQuestion = getAMultipleChoiceQuestionFromRequest(request);

        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of(savedQuestion));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }

    @Test
    void createQuestionWithDuplicateText_shouldThrowIllegalArgumentException() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        // Create a question with the same text as the request
        MultipleChoiceQuestion existingQuestion = getAMultipleChoiceQuestionFromRequest(request);
        existingQuestion.setQuestionText(request.getQuestionText());

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of(existingQuestion));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }


    @Test
    void createQuestionWithInvalidSurvey_shouldThrowEntityNotFoundException() {
        // Arrange
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }

    @Test
    void createQuestionWithUnauthorizedUser_shouldThrowUnauthenticatedUserException() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(false);

        // Act & Assert
        assertThrows(UnauthenticatedUserException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }


    @Test
    void createQuestionWithInvalidData_shouldThrowIllegalArgumentException() {
        // Arrange
        MultipleChoiceQuestionCreateRequest invalidRequest = new MultipleChoiceQuestionCreateRequest();
        invalidRequest.setQuestionText(""); // Empty question text
        invalidRequest.setSurveyId(null);   // Invalid survey ID

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(invalidRequest, "user@example.com"));
    }

    @Test
    void createQuestionWithInvalidEmailFormat_shouldThrowIllegalArgumentException() {
        // Arrange
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(request, "invalid-email-format")
        );
    }


    //----- createQuestion -- question order

    @Test
    void createQuestion_shouldIncrementQuestionOrder() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);

        MultipleChoiceQuestion existingQuestion = getAMultipleChoiceQuestionFromRequest(request);
        existingQuestion.setQuestionText("Another question");
        existingQuestion.setQuestionOrder(3);

        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of(existingQuestion));

        MultipleChoiceQuestion savedQuestion = getAMultipleChoiceQuestionFromRequest(request);
        savedQuestion.setQuestionOrder(4);

        when(questionRepository.save(any(MultipleChoiceQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getOrder());
    }

    @Test
    void createQuestionWithInvalidQuestionOrder_shouldThrowIllegalArgumentException() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        // Existing question with invalid order
        MultipleChoiceQuestion existingQuestion = getAMultipleChoiceQuestionFromRequest(request);
        existingQuestion.setQuestionOrder(-1);  // Invalid order

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of(existingQuestion));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }

    @Test
    void createQuestionWithMinusOneOrder_shouldThrowIllegalArgumentException() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        MultipleChoiceQuestion savedQuestion = getAMultipleChoiceQuestionFromRequest(request);
        savedQuestion.setQuestionOrder(-1);
        when(questionRepository.save(any(MultipleChoiceQuestion.class))).thenReturn(savedQuestion);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                questionService.createQuestion(request, "user@example.com"));
    }

    @Test
    void createQuestionWhenNoQuestionsExist_shouldSetQuestionOrderToOne() {
        // Arrange
        Survey survey = getASurvey();
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();

        // No existing questions in the survey
        when(surveyRepository.findById(101L)).thenReturn(Optional.of(survey));
        when(surveyOwnershipChecker.isOwner(101L, "user@example.com")).thenReturn(true);
        when(questionRepository.findAllBySurveyId(101L)).thenReturn(Set.of());

        MultipleChoiceQuestion savedQuestion = getAMultipleChoiceQuestionFromRequest(request);
        savedQuestion.setQuestionOrder(1);

        when(questionRepository.save(any(MultipleChoiceQuestion.class))).thenReturn(savedQuestion);

        // Act
        QuestionDTO result = questionService.createQuestion(request, "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getOrder());
    }

}
