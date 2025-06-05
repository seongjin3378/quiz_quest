package com.seong.portfolio.quiz_quest.problems.service.probWrite.probWrite;

import com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.problems.vo.ProbExecutionVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
* initializeProbExecutionVO
* - TestCaseList에 input값과 output String 값들을 \\n 문자열을 \n 문자열로 바꾸어 세로로 입력받을 수 있게 문자열을 replace함
* ex) 5
*     2
*     3
* - ProbExecutionVO에 set 함수를 이용해 file(소스파일), 사용언어, 정제한 TestCaseList를 설정
* - 클라이언트에서 <script>(DOM XSS)과 비슷한 값들을 삭제함, 따라서 정규식을 용해 <**> 이러한 형태에 문자열을 삭제함
* 
* */

/*
* validateProbExecution
* - 음수 값 및 10초 이하에 TimeLimit만 설정 가능
* - 128, 256, 512, 1024 단위로만 MemoryLimit 설정 가능
* - 최대 10개 이하에 입/출력 값만 입력 가능
* - isVisible 체크박스를 1개 이상 선택해야하며, 체크박스를 전부 다 선택하면 안됨
* */


/*
* executeProblem
* - ProbDockerExecution(IMpl: ProbWriteDockerExecution)를 실행함
* - 위에 로직이 문제 없시 실행 시 문제와 테스트케이스를 저장함
* */


/*
* saveProblemVisualAids
* - 이미지 파일이 있을 경우
*   - MultipartFileUtil 를 이용해 이미지를 생성함
*   - 이미지 파일에 상대경로를 List로 반환
* - 표 파일이 하나도 없을 경우
*   - VisualTables를 null 처리
*
* problemVisual 테이블에 저장 요청

* */

@Deprecated // 안씀
public interface ProbWrite {
    void initializeProbExecutionVO(ProbExecutionVO probExecutionVO, MultipartFile file, String language);
    void validateProbExecution(ProbExecutionVO probExecutionVO);
    long executeProblem(ProbExecutionVO probExecutionVO) throws IOException;
    void saveProblemVisualAids(MultipartFile[] files, ProbVisualVO probVisualVO, long problemId, String fileName) throws IOException;
}

