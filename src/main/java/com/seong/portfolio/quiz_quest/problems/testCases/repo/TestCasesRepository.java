package com.seong.portfolio.quiz_quest.problems.testCases.repo;


import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TestCasesRepository {
    void save(ProbExecutionVO probExecutionVO);
}
