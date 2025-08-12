package com.seong.portfolio.quiz_quest.courses.controller;


import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.comments.service.CommentService;
import com.seong.portfolio.quiz_quest.likes.service.LikesService;
import com.seong.portfolio.quiz_quest.courses.repo.CoursesRepository;
import com.seong.portfolio.quiz_quest.likes.dto.LikesDTO;
import com.seong.portfolio.quiz_quest.likes.dto.totalLikesInfoDTO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import com.seong.portfolio.quiz_quest.visual.dto.SaveProcessDTO;
import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import com.seong.portfolio.quiz_quest.visual.service.VisualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/api/v1/courses")
@Slf4j
public class CoursesRestController {
    private final CoursesRepository coursesRepository;
    private final VisualService visualService;
    private final LikesService likesService;

    private final CommentService commentService;

    public CoursesRestController(CoursesRepository coursesRepository, VisualService visualService, LikesService likesService, CommentService commentService) {
        this.coursesRepository = coursesRepository;
        this.visualService = visualService;
        this.likesService = likesService;
        this.commentService = commentService;
    }


    @PostMapping(value = "/write")
    @Transactional
    public ResponseEntity<String> writeCourses( @RequestPart(value="files",  required = false) MultipartFile[] files, @RequestPart("courseVO") CourseVO courseVO) throws IOException {

        coursesRepository.save(courseVO);
        long courseId = courseVO.getCourseId(); //courseId 생성 auto generated Key Value


        visualService.saveProcess(getSaveProcessDTO(files, "problem" + courseId + "-", getVisualDTO(courseId)));
        String returnUrl = "/c/n/"+courseId;
        return ResponseEntity.ok(returnUrl);
    }

    private VisualDTO getVisualDTO(long boardId)
    {
        VisualDTO visualDTO = new VisualDTO();
        visualDTO.setBoardId(boardId);
        visualDTO.setBoardType("course");
        visualDTO.setOnlyCaption(false);
        return visualDTO;
    }

    private SaveProcessDTO getSaveProcessDTO(MultipartFile[] files, String fileName, VisualDTO dto) throws IOException
    {

        return SaveProcessDTO.builder()
                .files(files)
                .visualDTO(dto)
                .fileName(fileName)
                .build();
    }

    @PostMapping(value = "/like/{courseId}/{likeCount}")
    @Transactional
    public ResponseEntity<totalLikesInfoDTO> increaseLikeOrCancel(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable long courseId, @PathVariable int likeCount) throws IOException
    {

        return ResponseEntity.ok(likesService.likeStatusProcess(createCourseLikesVO(principal.getUserNum(), courseId, likeCount)));

    }

    private LikesDTO createCourseLikesVO(long userNum, long courseId, int likeCount)
    {
        return LikesDTO.builder()
                .userNum(userNum)
                .boardId(courseId)
                .boardType("course")
                .likeCount(likeCount)
                .build();
    }


    @PostMapping(value = "/{courseId}/{cursor}/{sortType}/comments")
    public ResponseEntity<List<Object>> loadComments(@PathVariable long courseId, @PathVariable String cursor, @PathVariable String sortType) throws  IOException {
        List<Object> result = commentService.findComments(courseId, sortType, cursor, "course");
        return ResponseEntity.ok( result);
    }

    @PostMapping("/{sortType}/comments")
    public ResponseEntity<List<Object>> saveAndReturnProblemComments(@RequestBody CommentsDTO commentsDTO, @PathVariable String sortType) {
        commentsDTO.setBoardType("course");

        List<Object> result = commentService.saveAndReturnComments(commentsDTO, sortType);
        return ResponseEntity.ok(result);
    }


    @PatchMapping("/{commentId}/comments")
    public ResponseEntity<Object> updateComments(@PathVariable long commentId, @RequestBody CommentsDTO commentsDTO) {

        try {
            commentsDTO.setBoardType("course");
            commentsDTO.setCommentId(commentId);
            CommentsDTO result = commentService.updateCommentsAndReturn(commentsDTO);
            log.info("result: {}", result.toString());
            return ResponseEntity.ok(result);
        } catch (Exception e) {

            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 수정 요청이 실패하였습니다.");
        }
    }



    @DeleteMapping("/{boardId}/comments")
    public ResponseEntity<String> deleteComments(@PathVariable long boardId) {
        try {
            commentService.deleteComments(boardId);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {

            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 삭제 요청이 실패하였습니다.");
        }
    }

}
