package com.seong.portfolio.quiz_quest.problems.vo;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.problems.info.testCases.vo.TestCasesVO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProbExecutionVO {
    private String uuid;
    private long memoryLimit;
    private long nanoCpus;
    private int xp;
    private String language;
    private CreateContainerResponse container;
    private List<TestCasesVO> testCases;
    private MultipartFile file;
    private int timeLimit;
    private long problemId;
    private String problemTitle;
    private String problemType;
    private String problemContent;
    private ProblemVO problemVO;


}
