package com.seong.portfolio.quiz_quest.admin.problems.controller;


import com.seong.portfolio.quiz_quest.problems.service.probWrite.ProbWriteExecutionService;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo.ProbVisualVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/api/v1/problem")
@Slf4j
@RequiredArgsConstructor
public class AdminRestProblemController {
    private final ProbWriteExecutionService probWriteExecutionService;



    @PostMapping("/{language}/upload-and-validate") //file은 소스코드 files는 이미지파일들
    public ResponseEntity<String> writeProblem(@PathVariable String language, @RequestPart("file") MultipartFile file,  @RequestPart(value="files",  required = false) MultipartFile[] files, @RequestPart("probExecutionVO")ProbExecutionVO probExecutionVO, @RequestPart("probVisualVO") ProbVisualVO probVisualVO) throws IOException {
        log.info(probVisualVO.getVisualTables());
        return probWriteExecutionService.execute(probExecutionVO, file, files, probVisualVO, language);

    }

}
