package com.seong.portfolio.quiz_quest.settings.Docker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class DockerConfig {

    @Bean
    public DockerClient openDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build(); //도커 기본 config 로 빌더 생성
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder() //
                .dockerHost(config.getDockerHost()) // 기본 도커 호스트로 설정
                .sslConfig(config.getSSLConfig()) // SSL 암호화
                .maxConnections(100) // 최대 커넥션 수
                .connectionTimeout(Duration.ofSeconds(10)) //요청이후 데몬 프로세스 연결 최대 시간 설정
                .responseTimeout(Duration.ofSeconds(45)) // 응답 받기 까지 소요되는 최대 시간
                .build();

        //Docker 클라이언트 생성 및 반환
        return DockerClientBuilder.getInstance(config).withDockerHttpClient(httpClient).build();

    }
}
