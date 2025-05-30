package com.seong.portfolio.quiz_quest.courses.repo;


import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoursesRepository {

    int save(CourseVO courseVO);



    CourseVO findByCourseId(@Param("courseId") long courseId);

    int count(@Param("column") String column, @Param("value") Object value); //reflex

    List<CourseVO> findAll(int index); // reflex

    List<CourseVO> findAllByColumnAndValue(@Param("index") int index, @Param("column") String columnValue, @Param("value") String Value); //reflex
}
