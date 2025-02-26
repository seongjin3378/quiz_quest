package com.seong.portfolio.quiz_quest.problems.testCases.service;

import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCasesValidateImpl implements TestCasesValidate {

    @Override
    public void validateVisible(List<TestCasesVO> testCasesVOList) {
        String errorMessage = "Please check at least one checkbox, but do not select all checkboxes.";
        int isVisibleCount = 0;
        for (TestCasesVO testCasesVO : testCasesVOList) {
            if (testCasesVO.getIsVisible() == 1) {
                isVisibleCount++;
            }
        }
        if(isVisibleCount == 0 || isVisibleCount >= testCasesVOList.size()) { // 입/출력 체크박스를 선택 안했을 경우
            throw new IllegalArgumentException(errorMessage);
        }
        //모든 체크 박스를 선택 안했을 경우 true;
    }

    @Override
    public  void validateCount(List<TestCasesVO> testCasesVOList)
    {
        String errorMessage =  "You can only add up to 10 TestCases.";

        if(10 < testCasesVOList.size())
        {
            throw new IllegalArgumentException(errorMessage);
        }

    }
}
