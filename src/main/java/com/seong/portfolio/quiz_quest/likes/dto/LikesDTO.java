package com.seong.portfolio.quiz_quest.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikesDTO {
    private long userNum;
    private long boardId;
    private String boardType;
    private int likeCount;
    private LocalDateTime likedAt;
}

