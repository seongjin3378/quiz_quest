package com.seong.portfolio.quiz_quest.problems.service.probDockerExecution;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.service.probDocker.ProbDockerService;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.utils.ProbDockerUtils;
import com.seong.portfolio.quiz_quest.problems.utils.ProbFileUtils;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Qualifier("ProbDockerExecution")
public class ProbDockerExecutionServiceImpl implements ProbDockerExecutionService {
    private final ProblemRepository problemRepository;
    private final ProbDockerService probDockerService;
    private final ProbValidate probValidate;
    private final ProbFileUtils probFileUtils;

    @Transactional(readOnly = true)
    public void execute(ProbExecutionVO probExecutionVO) throws IOException {
        ProbExecutionVO executionVO = setProbExecutionVO(probExecutionVO);
        executeProblem(executionVO);
    }


    public ProbExecutionVO setProbExecutionVO(ProbExecutionVO probExecutionVO) {
        UUID uniqueUUID = UUID.randomUUID();
        String uuidString = uniqueUUID.toString();
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(probExecutionVO.getProblemId()).isVisible(-1).build());
        return ProbExecutionVO.builder()
                .uuid(uuidString)
                .memoryLimit((long) problemVO.getMemoryLimit() * 1024 * 1024)
                .nanoCpus(60_000_000L)
                .language(probExecutionVO.getLanguage())
                .testCases(problemVO.getTestCases())
                .file(probExecutionVO.getFile())
                .timeLimit(problemVO.getTimeLimit())
                .build();
    }

    public String getOutputValueAsString(List<TestCasesVO> TestCases) {
        StringBuilder result = new StringBuilder();
        for (TestCasesVO testCasesVO : TestCases) {
            result.append(testCasesVO.getOutputValue()).append("\n");
        }
        return result.toString();
    }

    public void executeProblem(ProbExecutionVO probExecutionVO) throws IOException {
        boolean isAnswerValid;
        boolean isTimeValid;
        try {
            CreateContainerResponse container = ProbDockerUtils.create(probDockerService, probExecutionVO);
            probExecutionVO.setContainer(container);
            String probResult = getOutputValueAsString(probExecutionVO.getTestCases());

            String result = ProbDockerUtils.execute(probDockerService, probExecutionVO);

            isAnswerValid = probValidate.validateAnswers(probResult, result);
            if (!isAnswerValid) throw new IllegalArgumentException("Answer is not valid!");
            isTimeValid = probValidate.validateTimeLimit(probExecutionVO.getTimeLimit());
            if (!isTimeValid) throw new IllegalArgumentException("Time is not valid!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            probFileUtils.delete(probExecutionVO.getUuid());
            ProbDockerUtils.terminate(probDockerService, probExecutionVO);
        }

    }
}
