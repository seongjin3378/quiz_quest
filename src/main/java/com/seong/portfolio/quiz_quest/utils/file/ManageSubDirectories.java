package com.seong.portfolio.quiz_quest.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class ManageSubDirectories {
    public static String manageSubdirectories(String parentDirectoryPath) {
        File parentDirectory = new File(parentDirectoryPath);
        File[] subdirectories = parentDirectory.listFiles(File::isDirectory);

        if (subdirectories != null) {
            return findOrCreateSubdirectory(subdirectories, parentDirectory);
        } else {
            log.info("상위 디렉토리가 비어있거나 존재하지 않습니다.");
            return createNewSubdirectory(parentDirectory, "1000");
        }
    }

    private static String findOrCreateSubdirectory(File[] subdirectories, File parentDirectory) {
        for (File subdir : subdirectories) {
            File[] files = subdir.listFiles();
            if (files != null && files.length <= 1000) {
                log.info("찾은 서브디렉토리: {}", subdir.getName());
                return subdir.getAbsolutePath();
            }
        }

        // 조건에 맞는 서브디렉토리가 없으면 새로 생성
        String newSubdirName = generateNewSubdirectoryName(subdirectories);
        return createNewSubdirectory(parentDirectory, newSubdirName);
    }

    private static String generateNewSubdirectoryName(File[] subdirectories) {
        if(subdirectories.length != 0) {
            String recentSubDir = subdirectories[subdirectories.length - 1].getName();
            return Integer.toString(Integer.parseInt(recentSubDir) + 1000); // 새 서브디렉토리 이름 생성
        }else{
            return "1000";
        }
    }

    private static String createNewSubdirectory(File parentDirectory, String subdirName) {
        File newSubdirectory = new File(parentDirectory, subdirName);
        if (newSubdirectory.mkdir()) {
            log.info("새 서브디렉토리 생성: {}", newSubdirectory.getName());
            return newSubdirectory.getAbsolutePath();
        } else {
            log.error("서브디렉토리 생성 실패.");
            return null;
        }
    }
}
