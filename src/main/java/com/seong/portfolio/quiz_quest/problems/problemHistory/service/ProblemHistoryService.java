package com.seong.portfolio.quiz_quest.problems.problemHistory.service;

public interface ProblemHistoryService {
    void saveProblem(long problemId);
    int isProblemSolved(long problemId, String userId);
}
