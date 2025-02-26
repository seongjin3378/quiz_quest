package com.seong.portfolio.quiz_quest.problems.service.probDockerExecution;

import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProbDockerExecutionService {
    void execute(ProbExecutionVO probExecutionVO) throws IOException; // 외부에서 이 메서드만 사용
    ProbExecutionVO setProbExecutionVO(ProbExecutionVO probExecutionVO);

    String getOutputValueAsString(List<TestCasesVO> TestCases);


    void executeProblem(ProbExecutionVO probExecutionVO) throws IOException;


}
