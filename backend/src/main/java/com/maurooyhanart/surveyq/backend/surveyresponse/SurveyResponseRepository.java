package com.maurooyhanart.surveyq.backend.surveyresponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    Set<SurveyResponse> findAllBySurveyId(Long surveyId);
    Set<SurveyResponse> findAllByUser_Email(String email);
}