package com.maurooyhanart.surveyq.backend.application;

import java.util.regex.Pattern;

public class PatternMatcher {
    public static void patternMatches(String emailAddress, String regexPattern) {
        if (!Pattern.compile(regexPattern).matcher(emailAddress).matches()) throw new IllegalArgumentException("Email format is invalid");
    }
}
