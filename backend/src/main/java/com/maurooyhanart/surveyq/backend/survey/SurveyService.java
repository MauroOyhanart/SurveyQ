package com.maurooyhanart.surveyq.backend.survey;

import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import com.maurooyhanart.surveyq.backend.survey.request.SurveyRequest;
import com.maurooyhanart.surveyq.backend.user.User;
import com.maurooyhanart.surveyq.backend.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SurveyService {
    private final Logger logger = LoggerFactory.getLogger(SurveyService.class);
    private final HttpLogger httpLogger;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    public SurveyService(SurveyRepository surveyRepository, UserRepository userRepository, HttpLogger httpLogger) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.httpLogger = httpLogger;
    }

    public List<SurveyDTO> getAllSurveys() {
        return surveyRepository.findAll().stream().map(SurveyDTO::toSurveyDto).toList();
    }

    public SurveyDTO createSurvey(SurveyRequest surveyRequest, String userEmail) {
        log("Survey: " + surveyRequest.getName());
        log("User: " + userEmail);

        User owner = userRepository.findByEmail(userEmail).orElseThrow(() -> {
            String errorText = "User not found for email: " + userEmail;
            return new EntityNotFoundException(errorText);
        });

        Survey survey = surveyRequest.toSurvey(owner);
        return SurveyDTO.toSurveyDto(surveyRepository.save(survey));
    }

    private void log(Object o) {
        logger.info(o.toString());
        httpLogger.httpLog("backend", o.toString(), "INFO");
    }
}
