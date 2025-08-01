package com.seong.portfolio.quiz_quest.likes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class totalLikesInfoDTO {
    private long boardId;
    private int totalLikes;
    private int totalDisLikes;
    private int currentState;
}
