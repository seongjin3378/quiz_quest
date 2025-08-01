package com.seong.portfolio.quiz_quest.courses.repo;


import com.seong.portfolio.quiz_quest.courses.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoursesRepository {

    int save(CourseVO courseVO);



    CourseVO findByCourseIdAndBoardType(@Param("courseId") long courseId, @Param("boardType") String boardType);

    int count(@Param("column") String column, @Param("value") Object value); //reflex

    List<CourseVO> findAll(@Param("index")int index, @Param("valueOfOnePage") int valueOfOnePage); // reflex

    List<CourseVO> findAllByColumnAndValue(@Param("index") int index, @Param("column") String columnValue, @Param("value") String Value, @Param("valueOfOnePage") int valueOfOnePage); //reflex
}
