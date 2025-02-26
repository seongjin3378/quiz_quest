package com.seong.portfolio.quiz_quest.admin.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminProblemVO {
    private String file; // 파일은 보통 별도의 처리가 필요하므로, String으로 저장할 경우 파일 이름을 저장
    private String memoryLimit;
    private String timeLimit;
    private String problemTitle;
    private String problemType;
    private String problemContent;
    private String language;
}
