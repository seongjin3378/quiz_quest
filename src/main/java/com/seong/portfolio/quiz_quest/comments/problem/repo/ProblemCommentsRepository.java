package com.seong.portfolio.quiz_quest.comments.problem.repo;

import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemCommentsRepository {
    long save(ProblemCommentsVO problemCommentsVO);
    List<ProblemCommentsVO> findAllByProblemId(@Param("problemId") long problemId, @Param("sortType") String sortType, @Param("cursor") String cursor);
    ProblemCommentsVO findByCommentId(@Param("commentId") long commentId);
    List<ProblemCommentsVO> findAllByParentCommentId(@Param("parentCommentId") long parentCommentId, @Param("sortType") String sortType, @Param("cursor") String cursor);
}
