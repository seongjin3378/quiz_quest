package com.seong.portfolio.quiz_quest.problems.service.probWrite;


import com.seong.portfolio.quiz_quest.problems.problemVisual.service.ProblemVisualService;
import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.problems.service.probWrite.probService.ProbWriteService;
import com.seong.portfolio.quiz_quest.problems.service.probWrite.probWrite.ProbWrite;
import com.seong.portfolio.quiz_quest.problems.testCases.service.TestCasesValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.utils.TestCasesFormatterUtil;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.user.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@Slf4j
public class ProbWriteExecutionService {
    private final TestCasesValidate testCasesValidate;
    private final ProbValidate probValidate;
    @Qualifier("ProbWriteDockerExecution")
    private final ProbDockerExecutionService probDockerExecutionService;
    private final ProbWriteService probWriteService;
    private final ProblemVisualService problemVisualService;
    //기본 어드민 문제 작성

    public ProbWriteExecutionService(@Qualifier("ProbWriteDockerExecution")ProbDockerExecutionService probDockerExecutionService, TestCasesValidate testCasesValidate, ProbValidate probValidate, ProbWriteService probWriteService, ProblemVisualService problemVisualService) {
        this.probDockerExecutionService = probDockerExecutionService;
        this.testCasesValidate = testCasesValidate;
        this.probValidate = probValidate;
        this.probWriteService = probWriteService;
        this.problemVisualService = problemVisualService;
    }

    @Transactional
    public ResponseEntity<String> execute(ProbExecutionVO probExecutionVO, MultipartFile file, MultipartFile[] files, ProbVisualVO probVisualVO, String language) {
        try {
            initializeProbExecutionVO(probExecutionVO, file, language);
            validateProbExecution(probExecutionVO);
            probDockerExecutionService.execute(probExecutionVO);
            long problemId = probWriteService.saveProblemAndTestCases(probExecutionVO);
            probVisualVO.setProblemId(problemId);
            problemVisualService.saveProblemVisualAids(files, probVisualVO, "problem" + problemId + "-");

            return ResponseEntity.ok("Yes");
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "Runtime error: Check if there are any syntax errors.";
            if (e.getMessage().contains("Illegal") || e.getMessage().contains("Please") || e.getMessage().contains("You")) {
                error = e.getMessage();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //사용자 문제 작성 - 미구현
    public ResponseEntity<String> execute(ProbExecutionVO probExecutionVO, MultipartFile file, MultipartFile[] files, ProbVisualVO probVisualVO, String language, String fileName) {
        return null;
    }

    private void initializeProbExecutionVO(ProbExecutionVO probExecutionVO, MultipartFile file, String language) {
        List<TestCasesVO> testCasesList = TestCasesFormatterUtil.getTestCasesWithReplace(probExecutionVO.getTestCases(), "\\n", "\n");
        probExecutionVO.setFile(file);
        probExecutionVO.setLanguage(language);
        probExecutionVO.setTestCases(testCasesList);
        String problemContent = TestCasesFormatterUtil.deleteAngleBrackets(probExecutionVO.getProblemContent());
        probExecutionVO.setProblemContent(problemContent);
    }

    private void validateProbExecution(ProbExecutionVO probExecutionVO) {
        probValidate.validateTimeLimit(probExecutionVO.getTimeLimit(), true);
        probValidate.validateMemoryLimit((int) probExecutionVO.getMemoryLimit());
        testCasesValidate.validateCount(probExecutionVO.getTestCases());
        testCasesValidate.validateVisible(probExecutionVO.getTestCases());
    }
}

