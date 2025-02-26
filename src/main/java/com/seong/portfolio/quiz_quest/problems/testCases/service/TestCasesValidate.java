package com.seong.portfolio.quiz_quest.problems.testCases.service;

import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;

import java.util.List;

public interface TestCasesValidate {
    void validateVisible(List<TestCasesVO> testCasesVOList);
    void validateCount(List<TestCasesVO> testCasesVOList);
}
