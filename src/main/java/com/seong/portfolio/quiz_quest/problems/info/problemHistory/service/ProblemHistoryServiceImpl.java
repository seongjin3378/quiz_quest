package com.seong.portfolio.quiz_quest.problems.info.problemHistory.service;

import com.seong.portfolio.quiz_quest.problems.info.problemHistory.repo.ProblemHistoryRepository;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemHistoryServiceImpl implements ProblemHistoryService {

    private final ProblemHistoryRepository problemHistoryRepository;
    private final SessionService sessionService;
    @Override
    public void saveProblem(long problemId) {
        String userId = sessionService.getSessionId();
        problemHistoryRepository.save(problemId, userId);
    }

    @Override
    public int isProblemSolved(long problemId, String userId) {
        return problemHistoryRepository.existsByProblemIdAndUserId(problemId, userId);
    }
}
