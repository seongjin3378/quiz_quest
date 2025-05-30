package com.seong.portfolio.quiz_quest.problems.service.probValidate;

import com.seong.portfolio.quiz_quest.docker.vo.DockerValidationData;
import com.seong.portfolio.quiz_quest.problems.problemVisual.vo.ProbVisualVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProbValidateImpl implements ProbValidate {
    private final SessionService sessionService;
    private final DockerValidationData dockerValidationData;
    @Override
    public boolean validateAnswers(String probResult, String userResult) {
        log.info("probResult: {}", probResult);
        log.info("userResult: {}", userResult);

        return probResult.trim() .equals(userResult.trim());
    }

    @Override
    public boolean validateTimeLimit(int probTimeLimit) {
        String userId = sessionService.getSessionId();
        int userTimeLimit = dockerValidationData.getTimeMap(userId);
        dockerValidationData.removeTimeMap(userId);
        return probTimeLimit >= userTimeLimit;
    }

    @Override
    public void validateMemoryLimit(int memoryLimit) {
        if(memoryLimit != 128 && memoryLimit != 256 && memoryLimit != 512 && memoryLimit != 1024)
        {
            throw new IllegalArgumentException("Please enter a memory limit of either 128, 256, 512, or 1024.");
        }
    }

    @Override
    public void validateTimeLimit(int probTimeLimit, boolean isWrite) {
        if(probTimeLimit < 0 || probTimeLimit >= 10000)
        {
            throw new IllegalArgumentException("Please set the time limit to either a negative value or within 10 seconds.");
        }
    }

    @Override
    public void validateVisual(MultipartFile[] files, ProbVisualVO vo) {
        if (isEmptyVisualTables(vo.getVisualTables())) { // 표를 추가 안했을 경우 null 처리
            vo.setVisualTables(null);
            log.info("표 추가 안함");
        }
        
        if(files == null || files.length == 0)
        {
            return;
        }


        int captionCount = vo.getVisualCaptions().size();
        if (captionCount != files.length) {
            log.info("캡션 입력 누락: captions={}, files={}", captionCount, files.length);
            throw new IllegalArgumentException("Please enter a caption for each image file.");
        }
    }
    private boolean isEmptyVisualTables(String visualTablesJson) {
        // "[]" 이외에도 공백(null/empty) 체크를 추가하면 더 안전
        return "[]".equals(visualTablesJson) || StringUtils.isBlank(visualTablesJson);
    }
}
