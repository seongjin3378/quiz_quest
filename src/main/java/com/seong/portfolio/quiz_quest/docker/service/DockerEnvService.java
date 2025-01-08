package com.seong.portfolio.quiz_quest.docker.service;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.seong.portfolio.quiz_quest.docker.vo.DockerVO;

import java.io.File;
/*createDockerFile
- 도커 파일을 생성하는 모듈
- 파이썬 버전, 작업 디렉토리 path, 소스파일 복사 및 cmd를 통해 언어와 어떤 코드를 실행 시킬 것인지 설정

* */

/*buildImage
- 콜백 함수를 통해 로그를 출력함
- 도커파일과 이미지 이름을 설정하여 도커 환경에서 이미지를 생성하는 역할
* */

/* createContainerAndStart
- 컨테이너 이름을 설정함
- 컨테이너 config를 설정함
  - withNanoCPUS:
    - 나노 코어 단위로 설정 1코어당 1,000,000,000
    - 0.1코어 = (100,000,000)로 설정

  - 컨테이너에 할당할 메모리를 설정
    - 512MB: 512 * 1024 * 1024L (바이트 단위로 512MB)
    - 1GB: 1 * 1024 * 1024 * 1024L (바이트 단위로 1GB)
    - 256MB: 256 * 1024 * 1024L (바이트 단위로 256MB)

  - withTty
    - TTY: 가상 터미널을 활성화하여 터미널을 사용하는 것처럼 사용자 입력을 받을 수 있음
    - withAttachStdin: 표준 입력을 컨테이너에 연결하여, 컨테이너 실행중에 입력 받을 수 있도록 함

*/
public interface DockerEnvService {
     File createDockerFile(DockerVO dockerVO);
     void buildImage(String imageName, File dockerFile);
     CreateContainerResponse createContainer(DockerVO vo);
}
