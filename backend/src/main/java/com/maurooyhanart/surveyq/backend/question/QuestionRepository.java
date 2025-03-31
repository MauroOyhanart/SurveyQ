package com.maurooyhanart.surveyq.backend.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    long countBySurveyId(Long surveyId);
    Set<Question> findAllBySurveyId(Long surveyId);
}
