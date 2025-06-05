package com.seong.portfolio.quiz_quest.problems.service.probWrite.probWrite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Deprecated
public class ProbWriteImpl  {
  /*  @Qualifier("ProbWriteDockerExecution")
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
    }*/

}
