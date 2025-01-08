package com.seong.portfolio.quiz_quest.problems.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.utils.ProbDockerUtils;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProblemExecutionService {
    private final ProblemRepository problemRepository;
    private final ProblemService problemService;

    public boolean execute(Long problemId, String language, MultipartFile file) throws IOException {
        ProbExecutionVO executionVO = setProbExecutionVO(problemId, language, file);

        return executeProblem(executionVO);
    }

    private ProbExecutionVO setProbExecutionVO(Long problemId, String language, MultipartFile file)
    {
        UUID uniqueUUID = UUID.randomUUID();
        String uuidString = uniqueUUID.toString();
        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(problemId).isVisible(-1).build());
        return new ProbExecutionVO(uuidString, problemVO.getMemoryLimit()* 1024 * 1024, 60_000_000L, language, null, problemVO.getTestCases(), file);
    }

    private boolean executeProblem(ProbExecutionVO probExecutionVO) throws IOException {
        CreateContainerResponse container = ProbDockerUtils.create(problemService, probExecutionVO);
        probExecutionVO.setContainer(container);
        String result = ProbDockerUtils.execute(problemService,  probExecutionVO);
        ProbDockerUtils.terminate(problemService, probExecutionVO);

        return true;
    }
}
