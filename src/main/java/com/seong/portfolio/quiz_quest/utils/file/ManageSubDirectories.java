package com.seong.portfolio.quiz_quest.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class ManageSubDirectories {

    /*
    * - properties에 설정된 경로인 File 객체 생성
    * - 현재 디렉토리에 디렉토리인 서브디렉토리들을 File[] 객체로 가져옴
    * - 서브디렉토리가 없을 경우
    *    - findOrCreateSubdirectory를 이용해 1000 이름을 가진 기본 폴더 생성하여 절대경로 반환
    * - 아닐 경우
    *    createNewSubdirectory를 이용해 폴더 생성 (기본 1000)
    * */
    public static String manageSubdirectories(String parentDirectoryPath) {
        File parentDirectory = new File(parentDirectoryPath);
        File[] subdirectories = parentDirectory.listFiles(File::isDirectory);

        if (subdirectories != null && subdirectories.length != 0 ) {
            return findOrCreateSubdirectory(subdirectories, parentDirectory);
        } else {
            log.info("상위 디렉토리가 비어있거나 존재하지 않습니다.");
            return createNewSubdirectory(parentDirectory, "1000");
        }
    }


    /*
    * 서브디렉토리를 for문으로 조회
    *  - 서브 디렉토리 안에 파일 갯수를 조회해 File[] 객체 생성
    *  - Files[] 객체가 null이 아니고 파일 객체가 1000개 이하인경우
    *    - 서브디렉토리 절대 경로 반환
    *
    * 1000개 이하 서브디렉토리를 못찾았을 경우
    * - 최근 서브디렉토리 객체(length -1)에 이름을 정수로 변환 시켜 1000을 더해 폴더이름을 String으로 반환(ex) recent:1000 -> 2000 이라는 String 반환)
    * - createNewSubdirectory 를 이용해 폴더 생성
    * */
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
        }
        
        return null;
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
