package com.seong.portfolio.quiz_quest.docker.vo;

import com.seong.portfolio.quiz_quest.docker.enums.DockerCompiler;
import com.seong.portfolio.quiz_quest.docker.enums.DockerLangExtension;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class DockerEnumVO {
    private final DockerLangExtension extension;
    private final DockerCompiler compiler;



    public static DockerEnumVO fromString(String language) {
        return switch (language.toLowerCase()) {
            case "python" -> new DockerEnumVO(DockerLangExtension.PYTHON, DockerCompiler.PYTHON);
            case "java" -> new DockerEnumVO(DockerLangExtension.JAVA, DockerCompiler.JAVA);
            case "c", "cpp", "c++" -> new DockerEnumVO(DockerLangExtension.CPLUSPLUS, DockerCompiler.CPLUSPLUS);
            default -> throw new IllegalArgumentException("지원하지 않는 언어입니다.");
        };
    }

}
