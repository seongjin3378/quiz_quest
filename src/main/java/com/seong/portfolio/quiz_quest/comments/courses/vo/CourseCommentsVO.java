package com.seong.portfolio.quiz_quest.comments.courses.vo;


import com.seong.portfolio.quiz_quest.comments.problem.vo.ReplyProblemCommentsVO;
import lombok.Data;

import java.util.List;

@Data
public class CourseCommentsVO {
    private Long commentId;          // 댓글 ID
    private Long courseId;
    private String commentContent;    // 댓글 내용
    private String createdAt;      // 생성 시간// 작성자
    private int level;
    private long userNum;
    private String author;
    private String cursor;
    private Long parentCommentId;
    private int replyCount;
}
