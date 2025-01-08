package com.seong.portfolio.quiz_quest.problems.utils;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.docker.vo.DockerEnumVO;
import com.seong.portfolio.quiz_quest.problems.service.ProblemService;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProbDockerUtils {


    public static CreateContainerResponse  create(ProblemService problemService, ProbExecutionVO probExecutionVO) throws IOException {
        DockerEnumVO dockerEnumVO = DockerEnumVO.fromString(probExecutionVO.getLanguage());
        problemService.saveCode(probExecutionVO.getFile(), dockerEnumVO.getExtension().getValue(), probExecutionVO.getUuid());
        File dockerFile = problemService.execCreateDockerFile(probExecutionVO.getLanguage(), probExecutionVO.getUuid());
        problemService.execBuildImage(probExecutionVO.getUuid(), dockerFile);
        return problemService.execCreateContainer(probExecutionVO.getUuid(), probExecutionVO.getNanoCpus(), probExecutionVO.getMemoryLimit(), probExecutionVO.getLanguage());
    }

    public static String execute(ProblemService problemService, ProbExecutionVO probExecutionVO) throws IOException {

        List<TestCasesVO> testCases = probExecutionVO.getTestCases();
        ArrayList<String> testInputs = new ArrayList<>();
        for(TestCasesVO testCase : testCases)
        {
            testInputs.add(testCase.getInputValue()+"\n");
        }
        return problemService.executeContainer(probExecutionVO.getContainer(), probExecutionVO.getLanguage(), probExecutionVO.getUuid(), testInputs);
    }

    public static void terminate(ProblemService problemService, ProbExecutionVO probExecutionVO)
    {
        //codes 데이터 삭제하는 알고리즘 추가
        problemService.terminateContainer(probExecutionVO.getContainer(), probExecutionVO.getUuid());
    }
}
