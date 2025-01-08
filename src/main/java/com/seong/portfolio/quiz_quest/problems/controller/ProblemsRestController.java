package com.seong.portfolio.quiz_quest.problems.controller;


import com.seong.portfolio.quiz_quest.docker.vo.DockerEnumVO;
import com.seong.portfolio.quiz_quest.problems.service.ProblemExecutionService;
import com.seong.portfolio.quiz_quest.problems.service.ProblemService;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemsRestController {

    private final ProblemExecutionService problemExecutionService;
    @PostMapping("/{problemId}/{language}/upload-and-validate")
    public ResponseEntity<String> saveProblemAndValidate(@PathVariable long problemId, @PathVariable String language, @RequestPart MultipartFile file) {

        try {

            problemExecutionService.execute(problemId, language, file);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed" + e.getMessage());
        }


        return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
    }
    }
