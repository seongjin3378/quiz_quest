package com.seong.portfolio.quiz_quest.problems.service.probWrite.probService;

import com.seong.portfolio.quiz_quest.visual.repo.VisualRepository;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.info.testCases.repo.TestCasesRepository;
import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.utils.file.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProbWriteServiceImpl implements ProbWriteService {
    @Value("${problem.image.upload.path}")
    private String ImageFilesPath;
    private final VisualRepository visualRepository;
    private final ProblemRepository problemRepository;
    private final TestCasesRepository testCasesRepository;

    @Override
    public void saveProblemVisualAids(MultipartFile[] files, VisualDTO visualDTO, String fileName) throws IOException {
        if (files != null) {
            List<String> results = MultipartFileUtil.saveFiles(files, ImageFilesPath, fileName);
            visualDTO.setVisualSrc(results);
        }
        if (visualDTO.getVisualTables().equals("[]")) { // 표를 추가 안했을 경우 null 처리
            visualDTO.setVisualTables(null);
        }
        visualRepository.save(visualDTO);
        //db 작업
    }

    @Override
    public long saveProblemAndTestCases(ProbExecutionVO probExecutionVO) {
        problemRepository.save(probExecutionVO);
        log.info("problemId {}", probExecutionVO.getProblemId());

        List<TestCasesVO> testcases = getTestCasesListWithProblemId(probExecutionVO);
        probExecutionVO.setTestCases(testcases);
        testCasesRepository.save(probExecutionVO);
        return probExecutionVO.getProblemId();
    }

    private List<TestCasesVO> getTestCasesListWithProblemId(ProbExecutionVO executionVO) {
        List<TestCasesVO> testcases = new ArrayList<>();
        for (TestCasesVO testcaseVO : executionVO.getTestCases()) {
            testcaseVO.setProblemId(executionVO.getProblemId());
            testcases.add(testcaseVO);
        }
        return testcases;
    }
}
