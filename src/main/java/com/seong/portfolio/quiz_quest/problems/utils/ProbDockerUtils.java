package com.seong.portfolio.quiz_quest.problems.utils;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.docker.vo.DockerEnumVO;
import com.seong.portfolio.quiz_quest.problems.service.ProbDockerService;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProbDockerUtils {


    public static CreateContainerResponse  create(ProbDockerService probDockerService, ProbExecutionVO probExecutionVO) throws IOException {
        DockerEnumVO dockerEnumVO = DockerEnumVO.fromString(probExecutionVO.getLanguage());
        probDockerService.saveCode(probExecutionVO.getFile(), dockerEnumVO.getExtension().getValue(), probExecutionVO.getUuid());
        File dockerFile = probDockerService.execCreateDockerFile(probExecutionVO.getLanguage(), probExecutionVO.getUuid());
        probDockerService.execBuildImage(probExecutionVO.getUuid(), dockerFile);
        return probDockerService.execCreateContainer(probExecutionVO.getUuid(), probExecutionVO.getNanoCpus(), probExecutionVO.getMemoryLimit(), probExecutionVO.getLanguage());
    }

    public static String execute(ProbDockerService probDockerService, ProbExecutionVO probExecutionVO) throws IOException {

        List<TestCasesVO> testCases = probExecutionVO.getTestCases();
        ArrayList<String> testInputs = new ArrayList<>();
        for(TestCasesVO testCase : testCases)
        {
            testInputs.add(testCase.getInputValue()+"\n");
        }

        return probDockerService.executeContainer(probExecutionVO.getContainer(), probExecutionVO.getLanguage(), probExecutionVO.getUuid(), testInputs);
    }

    public static void terminate(ProbDockerService probDockerService, ProbExecutionVO probExecutionVO) throws IOException
    {
        //codes 데이터 삭제하는 알고리즘 추가

        probDockerService.terminateContainer(probExecutionVO.getContainer(), probExecutionVO.getUuid());
    }
}
