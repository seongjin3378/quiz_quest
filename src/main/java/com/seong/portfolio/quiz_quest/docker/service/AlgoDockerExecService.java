package com.seong.portfolio.quiz_quest.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.seong.portfolio.quiz_quest.docker.utils.ParseTimeToMilliseconds;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;
import com.seong.portfolio.quiz_quest.docker.vo.DockerValidationData;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AlgoDockerExecService implements DockerExecService {
    private final DockerClient dockerClient;
    private final DockerValidationData dockerValidationData;
    private final SessionService sessionService;
    @Override
    public ProcessBuilder create(DockerVO dockerVO) throws IOException {
        String command = dockerVO.getLanguage();
        String containerName = dockerVO.getName();
        String scriptPath = dockerVO.getMCodePath();

        ProcessBuilder processBuilder = createProcessBuilder(command, containerName, scriptPath);
        processBuilder.redirectErrorStream(true); // 위 프로세스가 실행 하는 동안 에러 메시지를 표준 출력과 함께 호출


        return processBuilder;

    }
    private static ProcessBuilder createProcessBuilder(String command, String containerName, String scriptPath) {
        return switch (command.toLowerCase()) { // 대소문자 구분 없이 비교
            case "java" ->
                // Java 실행
                    new ProcessBuilder("docker", "exec", "-i", containerName, "bash", "-c", "time java -cp /app Main");
            case "c++" ->
                // C++ 실행
                    new ProcessBuilder("docker", "exec", "-i", containerName, "bash", "-c", "time /app/my_application " + scriptPath);
            case "python" ->
                // Python 실행
                    new ProcessBuilder("docker", "exec", "-i", containerName, "bash", "-c", "time python " + scriptPath);
            default -> throw new IllegalArgumentException("지원하지 않는 언어입니다: " + command);
        };
    }



    @Override
    public void processInput(DockerVO dockerVO, Process process) {
    try(OutputStream os = process.getOutputStream())
    {
        String exInput = dockerVO.getExInput();
        log.info("processInput {}", exInput);
        os.write(exInput.getBytes());
        os.flush();
    }catch(IOException e){
        log.error(e.getMessage());
        throw new RuntimeException("예시 값을 입력 중 오류가 발생했습니다!");
    }
    }

    @Override
    public String readContainerOutput(Process process) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        StringBuilder result = new StringBuilder();
        String regex = "^\\d+m\\d+(\\.\\d+)?s$";
        try {
            while ((line = reader.readLine()) != null) {
                /*타임 체크 하는 라인은 읽지 않음*/

                if(line.contains("Error response from daemon: container")){ // 컨테이너 안에 로직이 오류가 생길 경우
                    throw new RuntimeException("Runtime error: Check if there are any syntax errors.");
                }
                if(!line.contains("real") && !line.contains("user") && !line.contains("0m") && !line.contains("sys") && !line.isEmpty()) {

                result.append(line).append("\n");
                log.info(line);
                }
                else if(line.contains("user\t0m"))  //유저 cpu 읽는 시간을 추출
                {
                    line = line.replace("user\t", "");
                    int time = ParseTimeToMilliseconds.execute(line);
                    log.info(line);

                    log.info(String.valueOf(time));
                    dockerValidationData.setTimeMap(sessionService.getSessionId(), time);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());

        }
        return result.toString();

    }


    @Override
    public void terminate(CreateContainerResponse container, String imageName) {
        try {
            StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(container.getId());
            InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
            if (Boolean.TRUE.equals(containerInfo.getState().getRunning())) {
                stopContainerCmd.exec();
            }

            dockerClient.removeContainerCmd(container.getId()).withForce(true).exec();
            dockerClient.removeImageCmd(imageName).withForce(true).exec(); // 해당 이미지 강제 삭제
        } catch (Exception e) {
            log.error(e.getMessage());
        }finally {
            DockerCleanUp(); // 도커 파일 클린업
        }

    }

    private void DockerCleanUp()
    {
        List<Container> containers = dockerClient.listContainersCmd().withStatusFilter(Collections.singleton("exited")).exec();
        for (Container container : containers) {
            log.info("Deleting container: {}", container.getId());
            dockerClient.removeContainerCmd(container.getId()).exec();
        }

        List<Image> images = dockerClient.listImagesCmd().withDanglingFilter(true).exec();
        for (Image image : images) {
            log.info("Deleting dangling image: {}", image.getId());
            dockerClient.removeImageCmd(image.getId()).exec();
        }

    }
    }

