package com.seong.portfolio.quiz_quest.comments.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReplyCommentsDTO {
    private Long replyCommentId;        // 댓글 ID
    private String replyCommentContent; // 댓글 내용
    private String replyCreatedAt;      // 생성 시간 (String 타입으로 변경)
    private String replyAuthor;         // 작성자
    private int replyLevel;
}
