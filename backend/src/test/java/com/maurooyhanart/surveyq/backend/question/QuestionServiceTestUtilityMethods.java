package com.maurooyhanart.surveyq.backend.question;

import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestion;
import com.maurooyhanart.surveyq.backend.question.type.freeform.FreeFormQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.item.TextQuestionItem;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestion;
import com.maurooyhanart.surveyq.backend.question.type.multiplechoice.MultipleChoiceQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestion;
import com.maurooyhanart.surveyq.backend.question.type.poll.PollQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestion;
import com.maurooyhanart.surveyq.backend.question.type.rating.RatingQuestionCreateRequest;
import com.maurooyhanart.surveyq.backend.survey.Survey;

import java.util.List;

public class QuestionServiceTestUtilityMethods {
    //----------------------------------------------------
    //Utility methods
    public static Survey getASurvey() {
        Survey survey = new Survey();
        survey.setId(101L);
        return survey;
    }

    public static MultipleChoiceQuestionCreateRequest getAMultipleChoiceQuestionCreateRequest() {
        MultipleChoiceQuestionCreateRequest request = new MultipleChoiceQuestionCreateRequest();
        request.setSurveyId(101L);
        request.setQuestionText("Pick your favorite fruit");
        request.setTextItems(List.of(new TextQuestionItem("Apple"), new TextQuestionItem("Banana")));
        return request;
    }

    public static PollQuestionCreateRequest getAPollQuestionCreateRequest() {
        PollQuestionCreateRequest request = new PollQuestionCreateRequest();
        request.setSurveyId(101L);
        request.setQuestionText("Pick your favorite dish");
        request.setTextItems(List.of(new TextQuestionItem("Ice Cream"), new TextQuestionItem("Meat")));
        return request;
    }

    public static FreeFormQuestionCreateRequest getAFreeFormQuestionCreateRequest() {
        FreeFormQuestionCreateRequest request = new FreeFormQuestionCreateRequest();
        request.setSurveyId(101L);
        request.setQuestionText("Explain a topic");
        return request;
    }

    public static RatingQuestionCreateRequest getARatingQuestionCreateRequest() {
        RatingQuestionCreateRequest request = new RatingQuestionCreateRequest();
        request.setSurveyId(101L);
        request.setQuestionText("Rate these movies");
        request.setTextItems(List.of(new TextQuestionItem("Superman"), new TextQuestionItem("Batman")));
        return request;
    }



    public static MultipleChoiceQuestion getAMultipleChoiceQuestionFromRequest(MultipleChoiceQuestionCreateRequest request) {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        question.setId(1L);
        question.setSurveyId(101L);
        question.setQuestionText("Pick your favorite fruit");
        question.setQuestionItems(request.getTextItems());
        question.setQuestionOrder(1);
        return question;
    }

    public static PollQuestion getAPollQuestionFromRequest(PollQuestionCreateRequest request) {
        PollQuestion question = new PollQuestion();
        question.setId(1L);
        question.setSurveyId(101L);
        question.setQuestionText("Pick your favorite dish");
        question.setQuestionItems(request.getTextItems());
        question.setQuestionOrder(1);
        return question;
    }

    public static FreeFormQuestion getAFreeFormQuestionFromRequest(FreeFormQuestionCreateRequest request) {
        FreeFormQuestion question = new FreeFormQuestion();
        question.setId(1L);
        question.setSurveyId(101L);
        question.setQuestionText("Explain a topic");
        question.setQuestionOrder(1);
        return question;
    }

    public static RatingQuestion getARatingQuestionFromRequest(RatingQuestionCreateRequest request) {
        RatingQuestion question = new RatingQuestion();
        question.setId(1L);
        question.setSurveyId(101L);
        question.setQuestionText("Rate these movies");
        question.setQuestionItems(request.getTextItems());
        question.setQuestionOrder(1);
        return question;
    }
}
