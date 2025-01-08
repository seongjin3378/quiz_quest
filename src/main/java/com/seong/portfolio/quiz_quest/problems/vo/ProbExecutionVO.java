package com.seong.portfolio.quiz_quest.problems.vo;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProbExecutionVO {
    private String uuid;
    private long memoryLimit;
    private long nanoCpus;
    private String language;
    private CreateContainerResponse container;
    private List<TestCasesVO> testCases;
    MultipartFile file;

}
