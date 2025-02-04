package com.seong.portfolio.quiz_quest.problems.service;

public interface ProbValidateService {
    boolean validateAnswers(String probResult, String userResult);

    boolean validateTimeLimit(int probTimeLimit); //밀리 세컨드 단위로 비교
}