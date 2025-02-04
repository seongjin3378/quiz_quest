package com.seong.portfolio.quiz_quest.docker.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;

import java.io.IOException;
import java.util.ArrayList;

/* create
  컨테이너 이름, command(ex:python), 도커에서 마운트된 소스코드 경로를 이용해 프로세스 빌더를 생성함
*/

/* processInput
   가상 터미널에서 입력할 인자값을 OutPutStream을 이용해서 입력 시킴
*/

/*readContainerOutPut
   - InputStreamReader
     - 입력 스트림을 문자 스트림으로 변환
     - 바이트 스트림인 Process.getInputStream을 인자 값으로 받음
   - reader.readLine: 출력 결과 값을 라인 단위로 읽어서 String 으로 저장
   - 출력 결과 값을 ArrayList<String>으로 반환
   - 컨테이너를 종료시키는 시간이 오래 걸리기 때문에 실행 결과 값을
     반환 결과 값을 검증하여 terminate 함수가 실행 하기 전에 서버에서 클라이언트로 보내야함
*/

/* terminate
   - 컨테이너를 종료 시킨 후 컨테이너와 이미지를 삭제함
*/


public interface DockerExecService {
     ProcessBuilder create(DockerVO dockerVO) throws IOException;
     void processInput(DockerVO dockerVO, Process process);
     String readContainerOutput(Process process);
     void terminate(CreateContainerResponse container, String imageName);
}
