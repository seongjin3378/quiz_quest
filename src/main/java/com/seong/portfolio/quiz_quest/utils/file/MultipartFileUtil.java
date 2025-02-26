package com.seong.portfolio.quiz_quest.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MultipartFileUtil extends ManageSubDirectories {
    public static List<String> saveFiles(MultipartFile[] files, String uploadDir, String fileName) throws IOException {
        if (files.length == 0) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        } else {
            int index = 1;
            List<String> destinationDirs = new ArrayList<>();
            for (MultipartFile file : files) {
                String newFileName = fileName+index + "." + getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
                String newPath = manageSubdirectories(uploadDir); // 서브디렉토리 검색
                Path destinationFilePath = Paths.get(newPath, newFileName);
                try {
                    if (!destinationFilePath.toFile().exists()) {
                        Files.write(destinationFilePath, file.getBytes());
                        log.info("파일 생성 성공: {}", destinationFilePath.toAbsolutePath());
                        String relativePath = destinationFilePath.toAbsolutePath().toString().replace(uploadDir, "");
                        destinationDirs.add(relativePath);
                    } else {
                        log.error("파일이 이미 존재합니다: {}" , destinationFilePath.toAbsolutePath());
                    }
                } catch (IOException e) {
                    log.error("파일 생성 중 오류 발생: {}", e.getMessage());
                }
                index++;
            }
            return destinationDirs;
        }
    }


    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
