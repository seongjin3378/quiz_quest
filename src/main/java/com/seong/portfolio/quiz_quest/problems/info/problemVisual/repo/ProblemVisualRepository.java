package com.seong.portfolio.quiz_quest.problems.info.problemVisual.repo;


import com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo.ProbVisualVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemVisualRepository {
    void save(ProbVisualVO probVisualVO);
    @Select("SELECT visual_src FROM problem_visual WHERE problem_visual_id = #{problemVisualId};")
    String findVisualSrcByProblemVisualId(@Param("problemVisualId") long problemVisualId);
    List<ProbVisualVO> findAllVisualByProblemId(@Param("problemId") long problemId);
}
