package com.seong.portfolio.quiz_quest.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRepoDTO {
    private long commentId;
    private long boardId;
    private String sortType;
    private String cursor;
    private String boardType;
}
