package com.seong.portfolio.quiz_quest.visual.service;

import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@Slf4j
public class VisualValidateImpl implements VisualValidate{

    @Override
    public void  validateVisual(MultipartFile[] files, VisualDTO vo) {
        if (isEmptyVisualTables(vo.getVisualTables())) { // 표를 추가 안했을 경우 null 처리
            vo.setVisualTables(null);
            log.info("표 추가 안함");
        }

        if(files == null || files.length == 0)
        {
            return;
        }


        int captionCount = vo.getVisualCaptions().size();
        if (captionCount != files.length && vo.isOnlyCaption()) {
            log.info("캡션 입력 누락: captions={}, files={}", captionCount, files.length);
            throw new IllegalArgumentException("Please enter a caption for each image file.");
        }
    }
    private boolean isEmptyVisualTables(String visualTablesJson) {
        // "[]" 이외에도 공백(null/empty) 체크를 추가하면 더 안전
        return "[]".equals(visualTablesJson) || StringUtils.isBlank(visualTablesJson);
    }
}
