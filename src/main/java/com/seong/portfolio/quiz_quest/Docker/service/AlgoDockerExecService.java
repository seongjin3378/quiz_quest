package com.seong.portfolio.quiz_quest.Docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.seong.portfolio.quiz_quest.Docker.vo.DockerVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;


@Slf4j
@Service
@RequiredArgsConstructor
public class AlgoDockerExecService implements DockerExecService {
    private final DockerClient dockerClient;
    @Override
    public ProcessBuilder create(DockerVO dockerVO) throws IOException {
        String command = dockerVO.getLanguage();
        String containerName = dockerVO.getName();
        String scriptPath = dockerVO.getMCodePath();

        ProcessBuilder processBuilder = new ProcessBuilder("docker", "exec", "-i", containerName, command, scriptPath);
        processBuilder.redirectErrorStream(true); // 위 프로세스가 실행 하는 동안 에러 메시지를 표준 출력과 함께 호출


        return processBuilder;

    }


    @Override
    public void processInput(DockerVO dockerVO, Process process) {
    try(OutputStream os = process.getOutputStream())
    {
        for(String exInput : dockerVO.getExInput()) {
            log.info("processInput {}", exInput);
            os.write(exInput.getBytes());
            os.flush();
        }
    }catch(IOException e){
        log.error(e.getMessage());
        throw new RuntimeException("예시 값을 입력 중 오류가 발생했습니다!");
    }
    }

    @Override
    public ArrayList<String> readContainerOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        ArrayList<String> result = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                log.info(line);
                result.add(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage());

        }
        return result;

    }
    @Override
    public void terminate(CreateContainerResponse container, String imageName) {
        StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(container.getId());
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        if(Boolean.TRUE.equals(containerInfo.getState().getRunning())) {
            stopContainerCmd.exec();
        }

        dockerClient.removeContainerCmd(container.getId()).withForce(true).exec();
        dockerClient.removeImageCmd(imageName).withForce(true).exec(); // 해당 이미지 강제 삭제
    }
    }

