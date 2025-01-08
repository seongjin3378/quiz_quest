package com.seong.portfolio.quiz_quest.docker.enums;

public enum DockerLangExtension {
    PYTHON(".py"),    // Python 파일 확장자
    JAVA(".java"),     // Java 파일 확장자
    CPLUSPLUS(".cpp"); // C++ 파일 확장자

    private final String extension;

    DockerLangExtension(String extension) {
        this.extension = extension;
    }

    public String getValue() {
        return extension;
    }
}
