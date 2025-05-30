package com.seong.portfolio.quiz_quest.courses.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseViewVO {
    private long courseViewId;
    private long courseId;
    private long userNum;
    private int viewCount;
    private LocalDateTime recentViewTime;
}
