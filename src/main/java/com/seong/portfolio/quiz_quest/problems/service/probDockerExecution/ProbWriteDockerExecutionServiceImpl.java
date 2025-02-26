package com.seong.portfolio.quiz_quest.problems.service.probDockerExecution;


import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.testCases.repo.TestCasesRepository;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Qualifier("ProbWriteDockerExecution")
public class ProbWriteDockerExecutionServiceImpl implements ProbDockerExecutionService {
    private final ProblemRepository problemRepository;
    private final ProbDockerExecutionService probDockerExecutionService;
    private final TestCasesRepository testCasesRepository;

    public ProbWriteDockerExecutionServiceImpl(ProblemRepository problemRepository, @Qualifier("ProbDockerExecution") ProbDockerExecutionService probDockerExecutionService, TestCasesRepository testCasesRepository) {
        this.problemRepository = problemRepository;
        this.probDockerExecutionService = probDockerExecutionService;
        this.testCasesRepository = testCasesRepository;
    }

    // 외부에서 이것만 사용
    @Transactional
    public void execute(ProbExecutionVO probExecutionVO) throws IOException {
        ProbExecutionVO executionVO = setProbExecutionVO(probExecutionVO);
        executeProblem(executionVO);
        problemRepository.save(probExecutionVO);
        log.info("problemId {}", executionVO.getProblemId());
        List<TestCasesVO> testcases = getTestCasesListWithProblemId(probExecutionVO);
        probExecutionVO.setTestCases(testcases);
        testCasesRepository.save(probExecutionVO);
    }
    private List<TestCasesVO> getTestCasesListWithProblemId(ProbExecutionVO executionVO){
        List<TestCasesVO> testcases = new ArrayList<>();
        for(TestCasesVO testcaseVO : executionVO.getTestCases())
        {
            testcaseVO.setProblemId(executionVO.getProblemId());
            testcases.add(testcaseVO);
        }
        return testcases;
    }

    public ProbExecutionVO setProbExecutionVO(ProbExecutionVO probExecutionVO) {
        UUID uniqueUUID = UUID.randomUUID();
        String uuidString = uniqueUUID.toString();
        return ProbExecutionVO.builder()
                .uuid(uuidString)
                .memoryLimit(probExecutionVO.getMemoryLimit() * 1024 * 1024)
                .nanoCpus(60_000_000L)
                .language(probExecutionVO.getLanguage())
                .testCases(probExecutionVO.getTestCases())
                .file(probExecutionVO.getFile())
                .timeLimit(probExecutionVO.getTimeLimit())
                .problemTitle(probExecutionVO.getProblemTitle())
                .problemType(probExecutionVO.getProblemType())
                .problemContent(probExecutionVO.getProblemContent())
                .build();
    }

    public String getOutputValueAsString(List<TestCasesVO> TestCases) {
        return probDockerExecutionService.getOutputValueAsString(TestCases);
    }

    public void executeProblem(ProbExecutionVO probExecutionVO) throws IOException {
        try {
            probDockerExecutionService.executeProblem(probExecutionVO);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
