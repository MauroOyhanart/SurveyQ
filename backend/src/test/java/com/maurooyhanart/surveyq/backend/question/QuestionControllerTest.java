package com.maurooyhanart.surveyq.backend.question;

import static com.maurooyhanart.surveyq.backend.question.QuestionServiceTestUtilityMethods.getAMultipleChoiceQuestionCreateRequest;
import static com.maurooyhanart.surveyq.backend.question.QuestionServiceTestUtilityMethods.getAMultipleChoiceQuestionFromRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.maurooyhanart.surveyq.backend.JwtTestUtils;
import com.maurooyhanart.surveyq.backend.question.request.QuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestionCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Test
    public void getAllQuestions_shouldReturnAll() throws Exception {
        // Arrange
        QuestionDTO question1 = new QuestionDTO();
        question1.setId(1L);
        question1.setQuestionText("What is your favorite color?");

        QuestionDTO question2 = new QuestionDTO();
        question2.setId(2L);
        question2.setQuestionText("What is your favorite fruit?");

        List<QuestionDTO> questions = Arrays.asList(question1, question2);

        when(questionService.getAllQuestions()).thenReturn(questions);

        // Act & Assert
        mockMvc.perform(get("/public/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText").value("What is your favorite color?"))
                .andExpect(jsonPath("$[1].questionText").value("What is your favorite fruit?"));
    }

    @Test
    public void getAllQuestions_shouldReturnEmptyList() throws Exception {
        // Arrange
        when(questionService.getAllQuestions()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/public/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getAllQuestions_shouldThrowException() throws Exception {
        // Arrange
        when(questionService.getAllQuestions()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        mockMvc.perform(get("/public/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.errors['Runtime Exception']").value("Database connection failed"));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void createQuestion_shouldCreateQuestion() throws Exception {
        // Arrange
        MultipleChoiceQuestionCreateRequest request = getAMultipleChoiceQuestionCreateRequest();
        QuestionDTO createdQuestion = QuestionDTO.toQuestionDto(getAMultipleChoiceQuestionFromRequest(request));
        when(questionService.createQuestion(any(QuestionCreateRequest.class), any(String.class))).thenReturn(createdQuestion);

        // Act & Assert
        mockMvc.perform(post("/api/question")
                        .header("Authorization", "Bearer " + JwtTestUtils.mockJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.questionText").value("Pick your favorite fruit"));
    }
}