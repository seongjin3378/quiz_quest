package com.seong.portfolio.quiz_quest.admin.problems.controller;


import com.seong.portfolio.quiz_quest.admin.problems.service.AdminProblemService;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.service.TestCasesValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.utils.TestCasesFormatterUtil;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbTableVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbVisualVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/api/v1/problem")
@Slf4j
public class AdminRestProblemController {
    @Qualifier("ProbWriteDockerExecution")
    private final ProbDockerExecutionService probDockerExecutionService;
    private final TestCasesValidate testCasesValidate;
    private final ProbValidate probValidate;
    private final AdminProblemService adminProblemService;
    public AdminRestProblemController(@Qualifier("ProbWriteDockerExecution")ProbDockerExecutionService probDockerExecutionService, TestCasesValidate testCasesValidate, ProbValidate probValidate, AdminProblemService adminProblemService) {
        this.probDockerExecutionService = probDockerExecutionService;
        this.testCasesValidate = testCasesValidate;
        this.probValidate = probValidate;
        this.adminProblemService = adminProblemService;
    }




    @PostMapping("/{language}/upload-and-validate") //file은 소스코드 files는 이미지파일들
    public ResponseEntity<String> writeProblem(@PathVariable String language, @RequestPart("file") MultipartFile file,  @RequestPart("files") MultipartFile[] files, @RequestPart("probExecutionVO")ProbExecutionVO probExecutionVO, @RequestPart("probVisualVO") ProbVisualVO probVisualVO) throws IOException {
        adminProblemService.saveProblemImage(files, "테스트");

        List<TestCasesVO>  testCasesList = TestCasesFormatterUtil.getTestCasesWithReplace(probExecutionVO.getTestCases(), "\\n", "\n");
        probExecutionVO.setFile(file);
        probExecutionVO.setLanguage(language);
        probExecutionVO.setTestCases(testCasesList);
        String problemContent = TestCasesFormatterUtil.deleteAngleBrackets(probExecutionVO.getProblemContent());
        probExecutionVO.setProblemContent(problemContent);
        try {
            probValidate.validateTimeLimit(probExecutionVO.getTimeLimit(), true);
            probValidate.validateMemoryLimit((int) probExecutionVO.getMemoryLimit());
            testCasesValidate.validateCount(testCasesList);
            testCasesValidate.validateVisible(testCasesList);
            probDockerExecutionService.execute(probExecutionVO);
            return ResponseEntity.ok("Yes");
        }catch (Exception e) {
            log.error(e.getMessage());
            String error = "Runtime error: Check if there are any syntax errors.";
            if (e.getMessage().contains("Illegal") || e.getMessage().contains("Please") ||  e.getMessage().contains("You")) {
                error = e.getMessage();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
