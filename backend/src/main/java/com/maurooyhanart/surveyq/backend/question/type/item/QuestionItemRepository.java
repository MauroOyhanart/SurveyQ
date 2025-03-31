package com.maurooyhanart.surveyq.backend.question.type.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionItemRepository extends JpaRepository<QuestionItem, Long> {
    List<QuestionItem> findByQuestionId(Long questionId);
}
