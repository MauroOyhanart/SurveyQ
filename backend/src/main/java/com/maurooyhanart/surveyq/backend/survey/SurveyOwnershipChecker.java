package com.maurooyhanart.surveyq.backend.survey;

import org.springframework.stereotype.Component;

@Component
public class SurveyOwnershipChecker {
    private final SurveyRepository surveyRepository;

    public SurveyOwnershipChecker(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public boolean isOwner(Long surveyId, String userEmail) {
        return surveyRepository.findById(surveyId)
                .map(survey -> survey.getOwner().getEmail().equals(userEmail))
                .orElse(false);
    }
}
