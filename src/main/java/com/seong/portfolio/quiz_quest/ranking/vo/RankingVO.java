package com.seong.portfolio.quiz_quest.ranking.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RankingVO {
    private long pk;
    private String rankingType;
    private int rankingRank;
    private String userId;
    private int rankingScore;
}
