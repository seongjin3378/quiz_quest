package com.seong.portfolio.quiz_quest.problems.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.utils.ProbDockerUtils;
import com.seong.portfolio.quiz_quest.problems.utils.ProbFileUtils;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProbDockerExecutionService {
    private final ProblemRepository problemRepository;
    private final ProbDockerService probDockerService;
    private final ProbValidateService probValidateService;
    private final ProbFileUtils probFileUtils;


    public void execute(Long problemId, String language, MultipartFile file) throws IOException {
        ProbExecutionVO executionVO = setProbExecutionVO(problemId, language, file);
        executeProblem(executionVO);
    }

    private ProbExecutionVO setProbExecutionVO(Long problemId, String language, MultipartFile file)
    {
        UUID uniqueUUID = UUID.randomUUID();
        String uuidString = uniqueUUID.toString();
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(problemId).isVisible(-1).build());
        return new ProbExecutionVO(uuidString, problemVO.getMemoryLimit()* 1024 * 1024, 60_000_000L, language, null, problemVO.getTestCases(), file, problemVO.getTimeLimit());
    }
    private String getOutputValueAsString(List<TestCasesVO> TestCases)
    {
        StringBuilder result = new StringBuilder();
        for(TestCasesVO testCasesVO : TestCases )
        {
            result.append(testCasesVO.getOutputValue()).append("\n");
        }
        return result.toString();
    }

    private void executeProblem(ProbExecutionVO probExecutionVO) throws IOException {
        boolean isAnswerValid;
        boolean isTimeValid;
        try {
            CreateContainerResponse container = ProbDockerUtils.create(probDockerService, probExecutionVO);
            probExecutionVO.setContainer(container);
            String probResult = getOutputValueAsString(probExecutionVO.getTestCases());

            String result = ProbDockerUtils.execute(probDockerService, probExecutionVO);

            isAnswerValid = probValidateService.validateAnswers(probResult, result);
            if (!isAnswerValid) throw new IllegalArgumentException("Answer is not valid!");
            isTimeValid = probValidateService.validateTimeLimit(probExecutionVO.getTimeLimit());
            if (!isTimeValid) throw new IllegalArgumentException("Time is not valid!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            probFileUtils.delete(probExecutionVO.getUuid());
            ProbDockerUtils.terminate(probDockerService, probExecutionVO);
        }

    }
}
