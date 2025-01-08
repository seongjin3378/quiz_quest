package com.seong.portfolio.quiz_quest.docker.enums;

import lombok.Getter;


public enum DockerCompiler {
    PYTHON("python:3.9-slim"),
    JAVA("openjdk:11-jdk-slim"),
    CPLUSPLUS("gcc:latest"); // C++도 GCC를 사용할 수 있습니다.

    private final String compiler;

    DockerCompiler(String compiler) {
        this.compiler = compiler;
    }

    public String getValue() {
        return compiler;
    }

}
