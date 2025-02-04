package com.seong.portfolio.quiz_quest.problems.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class ProbFileUtils {
    @Value("${user.upload.path}")
    private  String dir;
    @PostConstruct
    public void init() {
        log.info("User upload path: {}", dir); // 확인용 출력
    }
    private String getFileNameWithoutExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return fileName; // 확장자가 없는 경우 원래 이름 반환
        }
        return fileName.substring(0, lastIndexOfDot); // 확장자 제거
    }

    public void delete(String fileName) {
        try {
            if (Files.isDirectory(Path.of(dir))) { //dir 있을경우
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(dir))) { //디렉토리 스트림 열기

                    for (Path entry : stream) {
                        String entryNameWithoutExt = getFileNameWithoutExtension(entry.getFileName().toString());
                        if (entryNameWithoutExt.equals(fileName)) {
                            Files.delete(entry); // 파일 삭제
                            log.info("Deleted {}", entry.getFileName());
                        }
                    }
                }
            } else {
                log.error("지정된 경로가 디렉토리가 아닙니다.");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
