package com.seong.portfolio.quiz_quest.courses.courseVisual.repo;

import com.seong.portfolio.quiz_quest.courses.vo.CourseVisualVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseVisualRepository {
    void save(CourseVisualVO courseVisualVO);
    @Select("SELECT visual_src FROM course_visual WHERE visual_url = #{visualUrl}")
    String findByVisualUrl(@Param("visualUrl") String visualUrl);
}
