package com.seong.portfolio.quiz_quest.courses.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseLikesVO {
    private long userNum;
    private long courseId;
    private int likeCount;
    private LocalDateTime likedAt;
}
