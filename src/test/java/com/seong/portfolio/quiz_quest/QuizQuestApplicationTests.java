package com.seong.portfolio.quiz_quest;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import com.seong.portfolio.quiz_quest.comments.problem.repo.ProblemCommentsRepository;
import com.seong.portfolio.quiz_quest.comments.problem.vo.ProblemCommentsVO;
import com.seong.portfolio.quiz_quest.docker.service.DockerEnvService;
import com.seong.portfolio.quiz_quest.docker.service.DockerExecService;
import com.seong.portfolio.quiz_quest.docker.vo.DockerEnumVO;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;
import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import com.seong.portfolio.quiz_quest.problems.repo.ProblemRepository;
import com.seong.portfolio.quiz_quest.problems.service.probDocker.ProbDockerService;
import com.seong.portfolio.quiz_quest.problems.service.probDockerExecution.ProbDockerExecutionService;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.utils.pagination.reflex.PaginationRepoReflex;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest(classes = QuizQuestApplication.class)
class QuizQuestApplicationTests {

    @Autowired
    @Qualifier("ProbWriteDockerExecution")
    private ProbDockerExecutionService probDockerExecutionService;

    @BeforeEach
    public void setUp() {
        // 사용자 정보를 설정
        UserDetails userDetails = User.withUsername("testUser")
                .password("password")
                .roles("USER") // 역할 설정
                .build();

        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void ProbDockerExecutionTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.py",
                "text/x-python",
                """
                a, b = input().split()
                
                # 입력 받은 값을 정수로 변환합니다.
                a = int(a)
                b = int(b)
                
                # 두 숫자를 더합니다.
                result = a + b
                
                # 결과를 출력합니다.
                print(result)
                """.getBytes()
        );

        // 파일 업로드 요청
        List<TestCasesVO> testCasesVOList = new ArrayList<>();
        testCasesVOList.add(TestCasesVO.builder().inputValue("2 3").outputValue("5").isVisible(1).build());
        testCasesVOList.add(TestCasesVO.builder().inputValue("3 4").outputValue("7").isVisible(1).build());
        testCasesVOList.add(TestCasesVO.builder().inputValue("5 7").outputValue("12").isVisible(0).build());
        probDockerExecutionService.execute(ProbExecutionVO
                .builder()
                .problemId(-1)
                .language("python")
                .file(file)
                        .memoryLimit(128)
                        .timeLimit(1000)
                        .testCases(testCasesVOList)
                        .problemTitle("아무개여")
                        .problemType("자료구조")
                        .problemContent("몰라여 그냥 짓는거임")
                .build());
    }

        // Clean up: Dockerfile 삭제

}




