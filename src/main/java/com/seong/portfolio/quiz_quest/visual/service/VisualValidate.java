package com.seong.portfolio.quiz_quest.visual.service;

import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import org.springframework.web.multipart.MultipartFile;

public interface VisualValidate {

    void validateVisual(MultipartFile[] files, VisualDTO vo);
}
