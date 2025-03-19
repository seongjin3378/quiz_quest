package com.seong.portfolio.quiz_quest.problems.service.probWrite.probWrite;

import com.seong.portfolio.quiz_quest.problems.service.probWrite.probService.ProbWriteService;
import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.probValidate.ProbValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.service.TestCasesValidate;
import com.seong.portfolio.quiz_quest.problems.testCases.utils.TestCasesFormatterUtil;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ProbWriteImpl implements ProbWrite {
    @Qualifier("ProbWriteDockerExecution")
    private final ProbDockerExecutionService probDockerExecutionService;
    private final TestCasesValidate testCasesValidate;
    private final ProbValidate probValidate;
    private final ProbWriteService probWriteService;

    public ProbWriteImpl(@Qualifier("ProbWriteDockerExecution")ProbDockerExecutionService probDockerExecutionService, TestCasesValidate testCasesValidate, ProbValidate probValidate, ProbWriteService probWriteService) {
        this.probDockerExecutionService = probDockerExecutionService;
        this.testCasesValidate = testCasesValidate;
        this.probValidate = probValidate;
        this.probWriteService = probWriteService;
    }

    public void initializeProbExecutionVO(ProbExecutionVO probExecutionVO, MultipartFile file, String language) {
        List<TestCasesVO> testCasesList = TestCasesFormatterUtil.getTestCasesWithReplace(probExecutionVO.getTestCases(), "\\n", "\n");
        probExecutionVO.setFile(file);
        probExecutionVO.setLanguage(language);
        probExecutionVO.setTestCases(testCasesList);
        String problemContent = TestCasesFormatterUtil.deleteAngleBrackets(probExecutionVO.getProblemContent());
        probExecutionVO.setProblemContent(problemContent);
    }
    public void validateProbExecution(ProbExecutionVO probExecutionVO) {
        probValidate.validateTimeLimit(probExecutionVO.getTimeLimit(), true);
        probValidate.validateMemoryLimit((int) probExecutionVO.getMemoryLimit());
        testCasesValidate.validateCount(probExecutionVO.getTestCases());
        testCasesValidate.validateVisible(probExecutionVO.getTestCases());
    }


    public long executeProblem(ProbExecutionVO probExecutionVO) throws IOException {
        probDockerExecutionService.execute(probExecutionVO);
        return probWriteService.saveProblemAndTestCases(probExecutionVO);
    }


    public void saveProblemVisualAids(MultipartFile[] files, ProbVisualVO probVisualVO, long problemId, String fileName) throws IOException {
        probVisualVO.setProblemId(problemId);
            if(probVisualVO.getVisualCaptions().size() != files.length)
            {
                log.info("캡션 입력 안함");
                throw new IllegalArgumentException("Please enter a caption for the image files.");

            }

        probWriteService.saveProblemVisualAids(files, probVisualVO, fileName);
    }

}
