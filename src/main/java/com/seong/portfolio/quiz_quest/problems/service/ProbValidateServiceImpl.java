package com.seong.portfolio.quiz_quest.problems.service;

import com.seong.portfolio.quiz_quest.docker.vo.DockerValidationData;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProbValidateServiceImpl implements ProbValidateService {
    private final SessionService sessionService;
    private final DockerValidationData dockerValidationData;
    @Override
    public boolean validateAnswers(String probResult, String userResult) {
        log.info("probResult: {}, userResult: {}", probResult, userResult);

        return probResult.equals(userResult);
    }

    @Override
    public boolean validateTimeLimit(int probTimeLimit) {
        String userId = sessionService.getSessionId();
        int userTimeLimit = dockerValidationData.getTimeMap(userId);
        dockerValidationData.removeTimeMap(userId);
        return probTimeLimit >= userTimeLimit;
    }
}
