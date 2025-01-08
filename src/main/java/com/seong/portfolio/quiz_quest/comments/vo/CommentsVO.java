package com.seong.portfolio.quiz_quest.comments.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@Builder
public class CommentsVO {
    private Long commentId;          // 댓글 ID
    private Long parentCommentId;    // 부모 댓글 ID (null 가능)
    private String commentContent;    // 댓글 내용
    private String author;            // 작성자
    private Timestamp createdAt;      // 생성 시간
}
