package com.seong.portfolio.quiz_quest.problems.service.probValidate;

import com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo.ProbVisualVO;
import org.springframework.web.multipart.MultipartFile;

public interface ProbValidate {
    boolean validateAnswers(String probResult, String userResult);

    boolean validateTimeLimit(int probTimeLimit); //밀리 세컨드 단위로 비교
    void validateMemoryLimit(int memoryLimit);
    void validateTimeLimit(int probTimeLimit, boolean isWrite);
    void validateVisual(MultipartFile[] files, ProbVisualVO vo);
}