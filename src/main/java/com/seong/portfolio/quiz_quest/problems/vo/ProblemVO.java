package com.seong.portfolio.quiz_quest.problems.vo;


import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
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
    private int xp;
    private List<TestCasesVO> testCases;
    private long testCaseId;
    private int isVisible;
   private int index;
}
