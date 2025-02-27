package com.seong.portfolio.quiz_quest.problems.service.probWrite.probService;

import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProbWriteService {
    void saveProblemVisualAids(MultipartFile[] files, ProbVisualVO probVisualVO, String fileName) throws IOException;

    long saveProblemAndTestCases(ProbExecutionVO probExecutionVO);
}
