package com.seong.portfolio.quiz_quest.problems.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemVO  {
    private long problemId;
    private String problemTitle;
    private String problemType;
    private String problemContent;
    private long testCaseId;
    private List<TestCasesVO> testCases;
    private int isVisible;
    private long memoryLimit;
    private int timeLimit;
    private int all_value;
    private int index;
}
