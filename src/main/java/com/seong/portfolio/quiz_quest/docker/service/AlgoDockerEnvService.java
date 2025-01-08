package com.seong.portfolio.quiz_quest.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.HostConfig;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlgoDockerEnvService implements DockerEnvService {
    private final DockerClient dockerClient;
    private final SessionService sessionService;


    @Override
    public File createDockerFile(DockerVO dockerVO) {
        String dockerFileContent = "FROM "+ dockerVO.getCompiler() +"\n" +
                "WORKDIR /app\n"+
                "COPY "+dockerVO.getName() +" .\n" +
                "CMD [\""+dockerVO.getLanguage()+"\", \""+dockerVO.getName()+"\"]";

        String path = dockerVO.getPath();
        File dockerFile = new File(path);
        try (FileWriter writer = new FileWriter(dockerFile)) {
            writer.write(dockerFileContent);
            return dockerFile;
        }catch  (IOException e)
        {
            log.error("오류 발생 ", e);
            throw new RuntimeException("Failed to create Docker file");

        }
    }

    @Override
    public void buildImage(String imageName, File dockerFile) {
        BuildImageResultCallback callback = new BuildImageResultCallback(){
            @Override
            public void onNext(BuildResponseItem item) {
                // 빌드 로그 출력
                log.info("BuildImage Log: {} ", item.getStream());
                super.onNext(item);
            }
        };

        try{
            dockerClient.buildImageCmd()
                    .withDockerfile(dockerFile)
                    .withTags(Collections.singleton(imageName))
                    .exec(callback)
                    .awaitCompletion();
        }catch (Exception e) {
            log.error("오류 발생 ", e);
            throw new RuntimeException("Failed to build Docker image");
        }
    }

    @Override
    public CreateContainerResponse createContainer(DockerVO vo) {
        String containerName = vo.getName();

        HostConfig hostConfig = HostConfig.newHostConfig().withNanoCPUs(vo.getNanoCpus()).withMemory(vo.getMemory());

        return dockerClient.createContainerCmd(vo.getImgName())
                .withName(containerName)
                .withTty(true)
                .withAttachStdin(true)
                .withHostConfig(hostConfig)
                .exec();
    }

}
