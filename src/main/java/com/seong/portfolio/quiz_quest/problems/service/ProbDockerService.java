package com.seong.portfolio.quiz_quest.problems.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface ProbDockerService {
    void saveCode(MultipartFile file, String extension, String uuId) throws IOException;

    File execCreateDockerFile(String language, String uuid);

    void execBuildImage(String imageName, File dockerFile);

    CreateContainerResponse execCreateContainer(String uuId, long nanoCpus, long memory, String language);

    String executeContainer(CreateContainerResponse container, String language, String uuId, ArrayList<String> exInputList) throws IOException;

    void terminateContainer(CreateContainerResponse container, String imageName);
}
