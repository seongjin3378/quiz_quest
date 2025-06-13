package com.seong.portfolio.quiz_quest.comments.courses.repo;


import com.seong.portfolio.quiz_quest.comments.courses.vo.CourseCommentsVO;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseCommentsRepository {

    long save(CourseCommentsVO courseCommentsVO);

    List<CourseCommentsVO> findAllByCourseIdAndAndSortTypeAndCursor(@Param("courseId") long courseId, @Param("sortType") String sortType, @Param("cursor") String cursor);

    List<CourseCommentsVO>  findAllByCommentIdAndCourseId(@Param("courseCommentsVO")CourseCommentsVO courseCommentsVO, @Param("largestCommentId") long largestCommentId);

    List<CourseCommentsVO> findAllByParentCommentId(@Param("parentCommentId") long parentCommentId, @Param("sortType") String sortType, @Param("cursor") String cursor);
}