package com.seong.portfolio.quiz_quest.problems.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemVO  {
    private long problemId;
    private String problemTitle;
    private String problemType;
    private String problemContent;
    private int memoryLimit;
    private int timeLimit;
    private List<TestCasesVO> testCases;
    private long testCaseId;
    private int isVisible;
   private int index;
}
