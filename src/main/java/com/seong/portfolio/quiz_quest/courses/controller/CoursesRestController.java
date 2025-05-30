package com.seong.portfolio.quiz_quest.courses.controller;


import com.seong.portfolio.quiz_quest.courses.courseVisual.service.CourseVisualService;
import com.seong.portfolio.quiz_quest.courses.repo.CoursesRepository;
import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import com.seong.portfolio.quiz_quest.utils.file.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CoursesRestController {
    private final CoursesRepository coursesRepository;
    private final CourseVisualService courseVisualService;
    @PostMapping(value = "/write")
    @Transactional
    public ResponseEntity<String> writeCourses( @RequestPart(value="files",  required = false) MultipartFile[] files, @RequestPart("courseVO") CourseVO courseVO  ) throws IOException {
        coursesRepository.save(courseVO);
        long courseId = courseVO.getCourseId(); //courseId 생성 auto generated Key Value
        courseVisualService.saveFiles(files, courseId, "courses" + courseId + "-");
        String returnUrl = "/c/n/"+courseId;
        return ResponseEntity.ok(returnUrl);
    }
}
