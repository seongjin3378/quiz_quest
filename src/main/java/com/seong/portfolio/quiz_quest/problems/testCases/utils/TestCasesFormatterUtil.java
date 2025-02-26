package com.seong.portfolio.quiz_quest.problems.testCases.utils;

import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;

import java.util.ArrayList;
import java.util.List;

public class TestCasesFormatterUtil {

    public static List<TestCasesVO> getTestCasesWithReplace(List<TestCasesVO> testCasesList, String target, String replacement) {
        List<TestCasesVO> replaceTestCasesList = new ArrayList<>();
        for (TestCasesVO testCasesVO : testCasesList) {
            String inputValue = testCasesVO.getInputValue().replace(target, replacement);
            String outputValue = testCasesVO.getOutputValue().replace(target, replacement);
            testCasesVO.setInputValue(inputValue);
            testCasesVO.setOutputValue(outputValue); // 수정된 부분
            replaceTestCasesList.add(testCasesVO);
        }
        return replaceTestCasesList;
    }

    public static String deleteAngleBrackets(String problemContent) {
        problemContent = problemContent.replaceAll("<.*?>", "");
        return problemContent;
    }

}
