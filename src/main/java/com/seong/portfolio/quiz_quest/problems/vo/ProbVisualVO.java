package com.seong.portfolio.quiz_quest.problems.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProbVisualVO {
    private String uploadedImageSrc;
    private List<String> uploadedImageCaptions; // 이미지 캡션
    private List<ProbTableVO> uploadedTables; // ProbTableVO 리스트
}
