package com.seong.portfolio.quiz_quest.problems.problemVisual.repo;


import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemVisualRepository {
    void save(ProbVisualVO probVisualVO);
}
