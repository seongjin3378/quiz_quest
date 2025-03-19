package com.seong.portfolio.quiz_quest.rankings.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RankingVO {
    private long pk;
    private String rankingType;
    private String userId;
    private int rankingScore;
    private LocalDateTime createdAt;
}
