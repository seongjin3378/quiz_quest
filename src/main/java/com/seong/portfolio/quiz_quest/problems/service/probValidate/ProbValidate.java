package com.seong.portfolio.quiz_quest.problems.service.probValidate;

public interface ProbValidate {
    boolean validateAnswers(String probResult, String userResult);

    boolean validateTimeLimit(int probTimeLimit); //밀리 세컨드 단위로 비교
    void validateMemoryLimit(int memoryLimit);
    void validateTimeLimit(int probTimeLimit, boolean isWrite);
}