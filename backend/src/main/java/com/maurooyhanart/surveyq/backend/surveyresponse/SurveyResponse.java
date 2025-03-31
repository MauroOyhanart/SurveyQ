package com.maurooyhanart.surveyq.backend.surveyresponse;

import com.maurooyhanart.surveyq.backend.questionresponse.QuestionResponse;
import com.maurooyhanart.surveyq.backend.survey.Survey;
import com.maurooyhanart.surveyq.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionResponse> questionResponses = new ArrayList<>();
}
