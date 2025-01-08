package com.seong.portfolio.quiz_quest.problems.testCases.vo;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestCasesVO {
    private long testCaseId;
    private String inputValue;
    private String outputValue;
}
