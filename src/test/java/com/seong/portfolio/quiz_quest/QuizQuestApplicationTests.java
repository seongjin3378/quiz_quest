package com.seong.portfolio.quiz_quest;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.seong.portfolio.quiz_quest.Docker.service.DockerEnvService;
import com.seong.portfolio.quiz_quest.Docker.service.DockerExecService;
import com.seong.portfolio.quiz_quest.Docker.vo.DockerVO;
import com.seong.portfolio.quiz_quest.ranking.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@SpringBootTest
class QuizQuestApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RankingRepository rankingRepository;
	/*
	@Transactional
	@Rollback(false) // 트랜잭션 롤백 방지
	@Test
	public void testBatchTransactionalInsert() {
		String insertDataSql = "INSERT INTO your_table (ranking_type, ranking_rank, user_id, ranking_score) VALUES (?, ?, ?, ?)";
		long startTime = System.currentTimeMillis();

		List<Object[]> batchArgs = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {
			batchArgs.add(new Object[]{
					"usage_time", // ranking_type
					i + 1,       // ranking_rank
					"user_" + i, // user_id
					100 + i      // ranking_score
			});
		}

		jdbcTemplate.batchUpdate(insertDataSql, batchArgs); // 배치 삽입

		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: " + (endTime - startTime) + " ms");
	}

	 /*
	//@Test
	public void DBTest()
	{
	 RankingVO ranking = RankingVO.builder().userId("test0").rankingType("usage_time").build();
	 rankingRepository.saveOrUpdateRanking(ranking);
	}

	 */

	/*
	@Test
	public void EnumTest()
	{
		for(RankingType rankingType : RankingType.values()) {
			System.out.println(rankingType.label());
		}
	}

	 */


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
    private DockerEnvService dockerEnvService;
    @Autowired
    private DockerExecService dockerExecService;

    @Autowired
    private SessionService sessionService;
    @Autowired
    private DockerClient dockerClient;
    @Test
    public void executeDockerService() throws IOException {

        File dockerFile = dockerEnvService.createDockerFile(DockerVO.builder()
                .compiler("python:3.9-slim")
                .name("my-python-script.py")
                .language("python")
                .path("src/main/resources/codes/")
                .build());

        dockerEnvService.buildImage(sessionService.getSessionId().toLowerCase(), dockerFile);

        CreateContainerResponse container = dockerEnvService.createContainerAndStart(
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

        ArrayList<String> arr = new ArrayList<>();
        arr.add("5\n");
        ArrayList<String> arr2 = new ArrayList<>();
        arr2.add("10\n");

        ArrayList<ArrayList<String>> arr3 = new ArrayList<>();
        arr3.add(arr);
        arr3.add(arr2);
        for(ArrayList<String> arr1 : arr3) {
                dockerExecService.processInput(DockerVO.builder()
                        .exInput(arr1).build(), process);

                dockerExecService.readContainerOutput(process);
                process.destroy();
                process = processBuilder.start();
        }
        //종료되기 전에 값을 SpringWebflux로 반환해야함
        //
        dockerExecService.terminate(container, sessionService.getSessionId().toLowerCase());

    }

    @Test
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
/*
    @Test
    public void IDE() throws IOException {
		/*
		try {
			// 수정할 파일 경로 설정 (리소스 경로)
			String fileName = "hello.py"; // 덮어쓸 파일 이름
			String resourcePath = "src/main/resources/codes/"; // 리소스 경로
			String filePath = resourcePath + fileName;

			// 덮어쓸 내용 정의
			String newContent = "from helloFunc2 import hello\n\n" +
					"if __name__ == '__main__':\n" +
					"    hello()";

			// 파일 덮어쓰기
			Path path = Paths.get(filePath);
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), false))) {
				writer.write(newContent); // 새로운 내용으로 덮어쓰기
			}

			System.out.println("Python 파일이 덮어씌워졌습니다: " + filePath);
		} catch (IOException e) {
			e.printStackTrace(); // 오류 메시지 출력
		}



        try {
            // Python 스크립트 실행을 위한 ProcessBuilder 설정
            ClassPathResource resource = new ClassPathResource("codes/hello.py");
            File scriptFile = resource.getFile();

            // 도커에서 실행할 경로를 설정합니다.
            //String scriptPath = "/app/codes/hello.py"; // 도커 컨테이너 내에서의 경로

            // ProcessBuilder를 사용하여 도커 컨테이너에서 스크립트를 실행합니다.
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "-v", scriptFile.getParent() + ":/app", // 부모 디렉토리(codes)를 /app으로 마운트
                    "python:3.9-slim", "python", "/app/" + scriptFile.getName() // 컨테이너 내에서 실행
            );

            processBuilder.redirectErrorStream(true); // 에러 스트림을 출력 스트림으로 리디렉션

            // 프로세스 시작
            Process process = processBuilder.start();

            // 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 프로세스 종료
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

 */


