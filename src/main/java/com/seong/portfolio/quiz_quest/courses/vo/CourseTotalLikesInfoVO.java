package com.seong.portfolio.quiz_quest.courses.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseTotalLikesInfoVO {
    private long courseId;
    private int totalLikes;
    private int totalDisLikes;
    private int currentState;
}
