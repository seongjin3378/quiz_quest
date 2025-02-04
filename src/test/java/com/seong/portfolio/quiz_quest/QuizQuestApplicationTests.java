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
import com.seong.portfolio.quiz_quest.problems.service.ProbDockerService;
import com.seong.portfolio.quiz_quest.problems.testCases.vo.TestCasesVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProblemVO;
import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import com.seong.portfolio.quiz_quest.utils.pagination.reflex.PaginationRepoReflex;
import com.seong.portfolio.quiz_quest.utils.pagination.vo.PaginationVO;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = QuizQuestApplication.class)

class QuizQuestApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RankingRepository rankingRepository;


    private static DockerClient openDockerClient() {
        // Docker 클라이언트 설정
        var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();

        // Apache HttpClient를 사용하여 HTTP 클라이언트 생성
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        // Docker 클라이언트 생성 및 반환
        return DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
    }

    @Autowired
    private SessionService sessionService;
    @Autowired
    private DockerClient dockerClient;


    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private DockerEnvService dockerEnvService;
    @Autowired
    private DockerExecService dockerExecService;

    private final Logger logger = LoggerFactory.getLogger(QuizQuestApplicationTests.class);

    @Autowired
    private ProbDockerService probDockerService;

    @Autowired
    private ProblemCommentsRepository problemCommentsRepository;


    @Autowired
    private PaginationRepoReflex paginationRepoReflex;


    public void DockerMeasureTest()
    {
        // Docker 명령어 설정
        String imageName = "<image_name>";  // 사용할 Docker 이미지 이름
        String command = "<your_command>";    // 실행할 명령어

        // ProcessBuilder 객체 생성
        ProcessBuilder processBuilder = new ProcessBuilder("docker", "run", "--rm", imageName, "/bin/sh", "-c", "time " + command);

        try {
            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 프로세스의 오류 출력 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void InnerJoinTest()
    {
        List<ProblemCommentsVO> problemCommentsVOList = problemCommentsRepository.findAllByProblemId(18L, "ASC", "82628991489999999999");

// 각 요소를 출력 , "DESC", " -2024011719999999999"
        for (ProblemCommentsVO comment : problemCommentsVOList) {
            logger.info("{} {}", comment.getCursor(), comment.getCommentId());
        }

    }
    public void PaginationRepositoryTest() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        PaginationVO<ProblemRepository, String> paginationVO = new PaginationVO<>();
        paginationVO.setRepository(problemRepository);
        paginationVO.setIndex(0);
        paginationVO.setColumn("problem_type");
        paginationVO.setValue(ProblemType.getDisplayNameByIndex(1));
        paginationVO.setValueOfOnePage(100);
        logger.info("{}", paginationRepoReflex.count(paginationVO));
    }


    public void ProblemServiceTest() throws IOException {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        File dockerFile = probDockerService.execCreateDockerFile("python", uuidString);
        probDockerService.execBuildImage(uuidString, dockerFile);
        CreateContainerResponse container = probDockerService.execCreateContainer(uuidString, 1_000_000_000L, 512 * 1024 * 1024, "python");

        ProblemVO problemVO = problemRepository.findByProblemId(ProblemVO.builder().problemId(18L).isVisible(-1).build());
        List<TestCasesVO> testCases = problemVO.getTestCases();
        ArrayList<String> testInputs = new ArrayList<>();
        for(TestCasesVO testCase : testCases)
        {
            testInputs.add(testCase.getInputValue()+"\n");
        }
        String result = probDockerService.executeContainer(container, "python", uuidString, testInputs);
        System.out.println(result);
        probDockerService.terminateContainer(container, uuidString);
    }


    public void EnumTest()
    {
        DockerEnumVO vo = DockerEnumVO.fromString("python");
        logger.info("language: {}, compiler: {}", vo.getExtension().getValue(), vo.getCompiler().getValue());
    }
    public void TestProblemsDB()
    {


        List<ProblemVO> problemsVOList = problemRepository.findAll(1);
        for (ProblemVO problemsVO : problemsVOList) {
            logger.info(problemsVO.getProblemTitle());
        }


        /*
        ProblemVO problemVO = problemsRepository.findByProblemId(18L);
        List<TestCasesVO> testCasesVOList = problemVO.getTestCases();

        for(TestCasesVO testCasesVO : testCasesVOList) {
            logger.info("problemVO input{}", testCasesVO.getInputValue());
        }

         */
    }




    public void executeDockerService() throws IOException {

        File dockerFile = dockerEnvService.createDockerFile(DockerVO.builder()
                .compiler("python:3.9-slim")
                .name("my-python-script.py")
                .language("python")
                .path("src/main/resources/codes/")
                .build());

        dockerEnvService.buildImage(sessionService.getSessionId().toLowerCase(), dockerFile);

        CreateContainerResponse container = dockerEnvService.createContainer(
                DockerVO.builder()
                        .name(sessionService.getSessionId().toLowerCase())
                        .hostDir("src/main/resources/codes/")
                        .containerDir("/app")
                        .imgName(sessionService.getSessionId().toLowerCase())
                        .nanoCpus(1_000_000_000L)
                        .memory((long) (512 * 1024 * 1024))
                        .language("python")
                        .mCodePath("/app/my-python-script.py")
                        .build()
        );

        dockerClient.startContainerCmd(container.getId()).exec();

        ProcessBuilder processBuilder = dockerExecService.create(DockerVO.builder()
                .language("python")
                .name(sessionService.getSessionId().toLowerCase())
                .mCodePath("/app/my-python-script.py").build()
        );
        Process process = processBuilder.start();


        ArrayList<String> arr3 = new ArrayList<>();
        arr3.add("5\n");
        arr3.add("10\n");
        for(String input : arr3) {
                dockerExecService.processInput(DockerVO.builder()
                        .exInput(input).build(), process);

                dockerExecService.readContainerOutput(process);
                process.destroy();
                process = processBuilder.start();
        }
        //종료되기 전에 값을 SpringWebflux로 반환해야함
        //
        dockerExecService.terminate(container, sessionService.getSessionId().toLowerCase());

    }




    public void Docker() throws IOException, InterruptedException {
        var config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();

        // Apache HttpClient를 사용하여 HTTP 클라이언트 생성
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        try (DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build()) {
            String pythonCode = "input_value = input(\"값을 입력하세요: \")\n" +
                    "for _ in range(5):\n" +
                    "    print(input_value)";

            // Python 파일 생성
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("my-python-script.py"))) {
                writer.write(pythonCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Dockerfile 생성
            String dockerfileContent = "FROM python:3.9-slim\n" +
                    "ARG INPUT_VALUE\n" +
                    "ENV INPUT_VALUE=${INPUT_VALUE}\n" +
                    "WORKDIR /app\n" +
                    "COPY my-python-script.py .\n" +
                    "CMD [\"python\", \"my-python-script.py\"]";

            String path = "src/main/resources/codes/";
            File dockerfile = new File(path+"Dockerfile");
            try (FileWriter writer = new FileWriter(dockerfile)){
                writer.write(dockerfileContent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            BuildImageResultCallback callback = new BuildImageResultCallback() {
                @Override
                public void onNext(BuildResponseItem item) {
                    // 빌드 로그 출력
                    System.out.println("Log: " + item.getStream());
                    super.onNext(item);
                }
            };

            try {
                // 이미지 빌드

                dockerClient.buildImageCmd()
                        .withDockerfile(dockerfile)
                        .withTags(Collections.singleton("my-python-image"))
                        .withBuildArg("INPUT_VALUE", "5")
                        .exec(callback)
                        .awaitImageId(); // 이미지 ID를 기다리기
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
            } finally {
                //dockerClient.close(); // 클라이언트 닫기
            }


            String containerName = "python-container-" + UUID.randomUUID();
            String hostDirectory = "src/main/resources/codes/";
            String containerDirectory ="/app";
            HostConfig hostConfig = HostConfig.newHostConfig().withNanoCPUs(1_000_000_000L).withMemory((long) (512 * 1024 * 1024));
                    //.withBinds(new Bind(hostDirectory, new Volume(containerDirectory)));

            CreateContainerResponse container = (CreateContainerResponse) dockerClient.createContainerCmd("my-python-image")
                    .withName(containerName)
                    .withTty(true)
                    .withAttachStdin(true)
                    .withHostConfig(hostConfig)
                    .withCmd("python", "/app/my-python-script.py")
                    .exec();

            dockerClient.startContainerCmd(container.getId()).exec();

            /*
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .withAttachStdin(true) // 표준 입력 활성화
                    .withTty(true) // TTY 활성화
                    .withCmd("python", "/app/my-python-script.py") // 스크립트 실행
                    .exec();
            */
            // 사용자 입력 제공



            // Exec 명령어 실행
            String command = "python"; // 실행할 명령어
            String scriptPath = "/app/my-python-script.py"; // Python 스크립트 경로

            try {
                // Docker exec 명령어 생성
                ProcessBuilder processBuilder = new ProcessBuilder("docker", "exec", "-i", containerName, command, scriptPath);
                processBuilder.redirectErrorStream(true);

                // 프로세스 시작
                Process process = processBuilder.start();

                // 사용자 입력 제공
                // 사용자 입력 제공
                try (OutputStream os = process.getOutputStream()) {
                    // 입력값과 엔터키를 제공
                    os.write("5\n".getBytes()); // 5 입력
                    os.flush();
                }

                // 컨테이너의 출력 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("23123131");
                // 프로세스 종료 대기
                StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(container.getId());
                stopContainerCmd.exec(); // 명령 실행
                System.out.println("종료완료");
                dockerClient.removeContainerCmd(container.getId()).exec();
                dockerClient.removeImageCmd("my-python-image").withForce(true).exec();

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        System.out.println(323232);
        }




        // Clean up: Dockerfile 삭제

    }




