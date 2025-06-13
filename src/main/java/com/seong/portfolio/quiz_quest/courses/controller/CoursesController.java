package com.seong.portfolio.quiz_quest.courses.controller;


import com.seong.portfolio.quiz_quest.courses.info.courseLikes.repo.CourseLikesRepository;
import com.seong.portfolio.quiz_quest.courses.info.courseVisual.service.CourseVisualService;
import com.seong.portfolio.quiz_quest.courses.enums.CourseTypeEnum;
import com.seong.portfolio.quiz_quest.courses.repo.CoursesRepository;
import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import com.seong.portfolio.quiz_quest.utils.pagination.service.PaginationService;
import com.seong.portfolio.quiz_quest.utils.pagination.utils.PaginationUtil;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CoursesController {
    private final PaginationService paginationService;
    private final CoursesRepository coursesRepository;
    private final CourseVisualService courseVisualService;
    private final CourseLikesRepository courseLikesRepository;

    
    @GetMapping("/c/{index}/s/{sortType}")
    public String courseBoardListV(@PathVariable int index, @PathVariable int sortType, Model model, HttpServletResponse response) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        PaginationUtil.handlePagination(response, model, paginationService, new PaginationVO.Builder<CoursesRepository, String>()
                .index(index)
                .repository(coursesRepository)
                .sortType(sortType)
                .column("course_type")
                .value(CourseTypeEnum.getDisplayNameByIndex(sortType))
                .valueOfOnePage(10)
                .url("/c/s")
                .pageItemCount(10)
                .build());

        return "courses";
    }


    @GetMapping("/c/n/{index}")
    public String courseBoardV(@PathVariable long index, Model model, @AuthenticationPrincipal PrincipalDetails principal) {
        CourseVO courseVO = coursesRepository.findByCourseId(index);
        Integer currentState = courseLikesRepository.findCurrentStateByUserNumAndCourseId(principal.getUserNum(), index);

        if(currentState == null) {
            currentState = 0;
        }

        log.info(courseVO.toString());
        model.addAttribute("course", courseVO);
        model.addAttribute("currentState", currentState);


        return "courseBoardView";
    }

    @GetMapping("/c/write")
    public String courseBoardWriteV(Model model) {

        List<String> courseTypeList = new ArrayList<>();

        for (CourseTypeEnum courseTypeEnum : CourseTypeEnum.values()) {
            courseTypeList.add(courseTypeEnum.getDisplayName());
        }
        model.addAttribute("courseTypeList", courseTypeList);
        return "courseBoardWrite";
    } 

    @GetMapping(value = "/c/pic/{UUID}")
    public ResponseEntity<Resource> coursesPictureV(@PathVariable String UUID, Model model) {
        try{
            Resource img = courseVisualService.loadAsResource(UUID);
            MediaType mediaType = MediaTypeFactory.getMediaType(img).orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .cacheControl(CacheControl
                    .maxAge(1, TimeUnit.HOURS))
                    .body(img); // 리소스 1시간 재사용 가능
        } catch (IOException e) {
            log.error(e.getMessage());
            return  ResponseEntity.badRequest().build();
        }

    }
}


