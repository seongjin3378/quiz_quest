package com.seong.portfolio.quiz_quest.courses.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseVisualVO {
    private long courseId;
    private List<String> visualSrc;
    private List<String> visualUrl;
}
