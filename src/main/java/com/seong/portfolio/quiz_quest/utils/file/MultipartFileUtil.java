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


/*
* 이미지 파일 객체를 for문으로 조회
* --------------------위 작업들을 반복 ---------------------------
* - newFileName을 지정한파일 이름 + 인덱스번호 +  . + 원본 파일 이름에 확장자(getFileExtension로 분리)로 지정
* - 서브 디렉토리를 검색
* - 생성 경로 Path 를 생성(newPath, newFileName)
* - 파일 생성
* - 상대 경로를 destinationDir List에 저장
 * - 인덱스 번호 추가
* ----------------위 작업들을 반복-------------------------------
* - destinationDir List를 return 으로 반환함
* */


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
                        Files.write(destinationFilePath, file.getBytes());
                        log.info("파일 생성 성공: {}", destinationFilePath.toAbsolutePath());
                        String relativePath = destinationFilePath.toAbsolutePath().toString().replace(uploadDir, "");
                        destinationDirs.add(relativePath);

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
