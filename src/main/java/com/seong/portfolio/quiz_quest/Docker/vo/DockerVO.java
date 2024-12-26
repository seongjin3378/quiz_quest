package com.seong.portfolio.quiz_quest.Docker.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Builder
@Setter
@Getter
public class DockerVO {
    private String path;
    private String name;
    private String imgName;
    private String codePath;
    private String compiler; //언어 컴파일러 버전
    private String language; //사용 언어
    private String hostDir;
    private String containerDir;
    private String mCodePath; // /app에 마운트된 codePath
    private long nanoCpus; //도커에서 사용할 cpu 개수
    private long memory; // 사용할 메모리
    private ArrayList<String> exInput; // 문제 예시 입력값 담는 변수
    private ArrayList<String> output; // 문제 결과 값 담는 변수
}
