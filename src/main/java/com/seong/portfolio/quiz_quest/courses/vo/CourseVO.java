package com.seong.portfolio.quiz_quest.courses.vo;

import com.seong.portfolio.quiz_quest.courses.enums.CourseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseVO {
    private long courseId;
    private String courseTitle;
    private String courseType;
    private String courseContent;
    private LocalDateTime createdAt;
    private int totalViews;
    private int totalLikes;
    private int totalDislikes;
}
