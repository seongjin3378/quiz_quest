package com.seong.portfolio.quiz_quest.problems.problemVisual.vo;

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
    private long problemVisualId;
    private List<String> visualSrc;
    private List<String> visualCaptions; // 이미지 캡션
    private String visualTables; // ProbTableVO 리스트
    private long problemId;
}
