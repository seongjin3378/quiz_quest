package com.seong.portfolio.quiz_quest.comments.courses.vo;


import lombok.Data;

@Data
@Deprecated
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

