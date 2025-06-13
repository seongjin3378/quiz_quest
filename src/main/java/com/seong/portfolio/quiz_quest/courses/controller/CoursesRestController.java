package com.seong.portfolio.quiz_quest.courses.controller;


import com.seong.portfolio.quiz_quest.comments.courses.vo.CourseCommentsVO;
import com.seong.portfolio.quiz_quest.comments.service.CommentService;
import com.seong.portfolio.quiz_quest.courses.info.courseLikes.service.CourseLikesService;
import com.seong.portfolio.quiz_quest.courses.info.courseVisual.service.CourseVisualService;
import com.seong.portfolio.quiz_quest.courses.repo.CoursesRepository;
import com.seong.portfolio.quiz_quest.courses.vo.CourseLikesVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseTotalLikesInfoVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final CourseVisualService courseVisualService;
    private final CourseLikesService courseLikesService;

    @Qualifier("CourseCommentService")
    private final CommentService commentService;

    public CoursesRestController(CoursesRepository coursesRepository, CourseVisualService courseVisualService, CourseLikesService courseLikesService, @Qualifier("CourseCommentService") CommentService commentService) {
        this.coursesRepository = coursesRepository;
        this.courseVisualService = courseVisualService;
        this.courseLikesService = courseLikesService;
        this.commentService = commentService;
    }


    @PostMapping(value = "/write")
    @Transactional
    public ResponseEntity<String> writeCourses( @RequestPart(value="files",  required = false) MultipartFile[] files, @RequestPart("courseVO") CourseVO courseVO  ) throws IOException {
        coursesRepository.save(courseVO);
        long courseId = courseVO.getCourseId(); //courseId 생성 auto generated Key Value
        courseVisualService.saveFiles(files, courseId, "courses" + courseId + "-");
        String returnUrl = "/c/n/"+courseId;
        return ResponseEntity.ok(returnUrl);
    }
    @PostMapping(value = "/like/{courseId}/{likeCount}")
    @Transactional
    public ResponseEntity<CourseTotalLikesInfoVO> increaseLikeOrCancel(@AuthenticationPrincipal PrincipalDetails principal, @PathVariable long courseId, @PathVariable int likeCount) throws IOException
    {

        return ResponseEntity.ok(courseLikesService.likeStatusProcess(createCourseLikesVO(principal.getUserNum(), courseId, likeCount)));

    }

    private CourseLikesVO createCourseLikesVO(long userNum, long courseId, int likeCount)
    {
        return CourseLikesVO.builder()
                .userNum(userNum)
                .courseId(courseId)
                .likeCount(likeCount)
                .build();
    }


    @PostMapping(value = "/{courseId}/{cursor}/{sortType}/comments")
    public ResponseEntity<List<Object>> loadComments(@PathVariable long courseId, @PathVariable String cursor, @PathVariable String sortType) throws IOException {
        log.info("23232");
        List<Object> result = commentService.findComments(courseId, sortType, cursor);
        return ResponseEntity.ok( result);
    }

    @PostMapping("/{sortType}/comments")
    public ResponseEntity<List<Object>> saveAndReturnProblemComments(@RequestBody CourseCommentsVO courseCommentsVO, @PathVariable String sortType) {

        List<Object> result = commentService.saveAndReturnComments(courseCommentsVO, sortType);
        return ResponseEntity.ok(result);
    }
}
