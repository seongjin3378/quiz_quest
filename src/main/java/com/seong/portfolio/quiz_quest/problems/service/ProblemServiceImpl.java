package com.seong.portfolio.quiz_quest.problems.service;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.docker.service.DockerEnvService;
import com.seong.portfolio.quiz_quest.docker.service.DockerExecService;
import com.seong.portfolio.quiz_quest.docker.vo.DockerEnumVO;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final SessionService sessionService;
    @Value("${user.upload.path}")
    private String uploadPath;
    @Value("${container.dir}")
    private String containerDir;

    private final DockerEnvService dockerEnvService;
    private final DockerClient dockerClient;
    private final DockerExecService dockerExecService;
    @Override
    public void saveCode(MultipartFile file, String extension, String uuId) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }


        File destinationFile = new File(uploadPath, uuId + extension);
        log.info("destination File: {}", destinationFile.getAbsolutePath());
        file.transferTo(destinationFile);

    }

    @Override
    public File execCreateDockerFile(String language, String uuId) {
        DockerEnumVO vo = DockerEnumVO.fromString(language);
        return dockerEnvService.createDockerFile(DockerVO.builder()
                .compiler(vo.getCompiler().getValue())
                .name(uuId+vo.getExtension().getValue())
                .language(language)
                .path(uploadPath+ uuId)
                .build());
    }

    @Override
    public void execBuildImage(String imageName, File dockerFile) {
        dockerEnvService.buildImage(imageName, dockerFile);
    }

    @Override
    public CreateContainerResponse execCreateContainer(String uuId, long nanoCpus, long memory, String language) {
        DockerEnumVO vo = DockerEnumVO.fromString(language);
        log.info("UUID: {}", uuId);
        log.info("Host Directory: {}", uploadPath);
        log.info("Container Directory: {}", containerDir);
        log.info("Image Name: {}", uuId);
        log.info("NanoCPUs: {}", nanoCpus);
        log.info("Memory: {} bytes", memory);
        log.info("Language: {}", language);
        log.info("Code Path: {}", containerDir+"/"+uuId+vo.getExtension().getValue());

        return dockerEnvService.createContainer(
                DockerVO.builder()
                        .name(uuId)
                        .hostDir(uploadPath)
                        .containerDir(containerDir)
                        .imgName(uuId)
                        .nanoCpus(nanoCpus)
                        .memory(memory)
                        .language(language)
                        .mCodePath(containerDir+"/"+uuId+vo.getExtension().getValue())
                        .build()
        );
    }

    @Override
    public String executeContainer(CreateContainerResponse container, String language, String uuId, ArrayList<String> exInputList) throws IOException {
        dockerClient.startContainerCmd(container.getId()).exec();
        DockerEnumVO vo = DockerEnumVO.fromString(language);
        ProcessBuilder processBuilder = dockerExecService.create(DockerVO.builder()
                .language(language)
                .name(uuId)
                .mCodePath(containerDir+"/"+uuId+vo.getExtension().getValue()).build()
        );
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        for(String input : exInputList)
        {
            dockerExecService.processInput(DockerVO.builder()
                    .exInput(input).build(), process);
            output.append(dockerExecService.readContainerOutput(process));
            process.destroy();
            process = processBuilder.start();
        }
        return output.toString();
    }

    @Override
    public void terminateContainer(CreateContainerResponse container, String imageName) {
        dockerExecService.terminate(container, imageName);
    }


}
