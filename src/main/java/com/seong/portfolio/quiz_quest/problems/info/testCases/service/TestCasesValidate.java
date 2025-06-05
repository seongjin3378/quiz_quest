package com.seong.portfolio.quiz_quest.problems.info.testCases.service;

import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;

import java.util.List;

public interface TestCasesValidate {
    void validateVisible(List<TestCasesVO> testCasesVOList);
    void validateCount(List<TestCasesVO> testCasesVOList);
}
