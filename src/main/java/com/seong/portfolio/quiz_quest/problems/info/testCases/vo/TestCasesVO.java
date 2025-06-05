package com.seong.portfolio.quiz_quest.problems.info.testCases.vo;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCasesVO {
    private long testCaseId; //pk
    private String inputValue;
    private String outputValue;
    private int isVisible;
    private long problemId; //fk
}
