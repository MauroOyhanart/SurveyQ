package com.maurooyhanart.surveyq.backend.questionresponse;

import com.maurooyhanart.surveyq.backend.surveyresponse.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Long> {
    Set<QuestionResponse> findAllBySurveyResponseIn(Set<SurveyResponse> surveyResponses);
}

