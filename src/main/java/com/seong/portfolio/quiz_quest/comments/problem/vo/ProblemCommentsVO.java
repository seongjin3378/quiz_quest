package com.seong.portfolio.quiz_quest.comments.problem.vo;

import lombok.*;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCommentsVO {
    private Long commentId;          // 댓글 ID
    private Long problemId;
    private String commentContent;    // 댓글 내용
    private String createdAt;      // 생성 시간// 작성자
    private int level;
    private String author;
    private String cursor;
    private List<ReplyProblemCommentsVO> replyCommentList;    // 부모 댓글 ID (null 가능)
    private Long parentCommentId;
    private int replyCount;
}
