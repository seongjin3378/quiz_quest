package com.seong.portfolio.quiz_quest.problems.repo;


import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemRepository {
    List<ProblemVO> findAll();
    ProblemVO findByProblemId(ProblemVO problemVO);
}
