package com.seong.portfolio.quiz_quest.problems.controller;


import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.service.ProblemCommentService;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ReplyProblemCommentsVO;
import com.seong.portfolio.quiz_quest.problems.service.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemsRestController {

    private final ProbDockerExecutionService probDockerExecutionService;
    private final ProblemCommentsRepository problemCommentsRepository;
    private final ProblemCommentService problemCommentService;

    @PostMapping("/{problemId}/{language}/upload-and-validate")
    public ResponseEntity<String> saveProblemAndValidate(@PathVariable long problemId, @PathVariable String language, @RequestPart MultipartFile file) {

        try {

            probDockerExecutionService.execute(problemId, language, file);
            return ResponseEntity.ok("Yes");
        } catch (Exception e) {
            String error = "Runtime error: Check if there are any syntax errors.";
            if (e.getMessage().contains("Illegal")) {
                error = e.getMessage();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{problemId}/{cursor}/{sortType}/comments")
    public ResponseEntity<List<ProblemCommentsVO>> getProblemComments(@PathVariable long problemId, @PathVariable String cursor, @PathVariable String sortType) {
        List<ProblemCommentsVO> problemCommentsVO = problemCommentsRepository.findAllByProblemId(problemId, sortType, cursor);

        return ResponseEntity.ok(problemCommentsVO);
    }


    @PostMapping("/{sortType}/comments")
    public ResponseEntity<List<ProblemCommentsVO>> saveAndReturnProblemComments(@RequestBody ProblemCommentsVO problemCommentsVO, @PathVariable String sortType) {

        List<ProblemCommentsVO> result = problemCommentService.saveAndReturnProblemComments(problemCommentsVO, sortType);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{parentCommentId}/{cursor}/{sortType}/reply-comments")
    public ResponseEntity<List<ProblemCommentsVO>> getReplyProblemComments(@PathVariable long parentCommentId, @PathVariable String cursor, @PathVariable String sortType) {
        List<ProblemCommentsVO> replyCommentsVO;
        if(cursor.equals("0")) {
            replyCommentsVO = problemCommentsRepository.findAllByParentCommentId(parentCommentId, sortType, "0");
        }else{
            replyCommentsVO = problemCommentsRepository.findAllByParentCommentId(parentCommentId, sortType, cursor);
        }

        return ResponseEntity.ok(replyCommentsVO);
    }
}
