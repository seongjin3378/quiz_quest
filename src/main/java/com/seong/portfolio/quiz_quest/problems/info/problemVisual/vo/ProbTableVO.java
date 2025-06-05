package com.seong.portfolio.quiz_quest.problems.info.problemVisual.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProbTableVO {
    private String caption; // 캡션
    private List<String[]> rows; // 데이터 행 리스트

}
