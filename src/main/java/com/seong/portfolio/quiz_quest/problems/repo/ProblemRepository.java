package com.seong.portfolio.quiz_quest.problems.repo;


import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
public interface ProblemRepository {
    int save(ProbExecutionVO probExecutionVO);

    List<ProblemVO> findAll(int index);

    ProblemVO findByProblemId(ProblemVO problemVO);

    int count(@Param("column") String column, @Param("value") Object value);

    List<ProblemVO> findAllByColumnAndValue(@Param("index") int index, @Param("column") String columnValue, @Param("value") String Value);
}
