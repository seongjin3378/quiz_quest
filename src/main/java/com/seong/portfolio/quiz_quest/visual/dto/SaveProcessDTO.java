package com.seong.portfolio.quiz_quest.visual.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProcessDTO {
    private MultipartFile[] files;
    private VisualDTO visualDTO;
    private String fileName;
}
