package com.seong.portfolio.quiz_quest.rankings.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisRankingVO {
    String key;
    String value;
    long maxScore;
    long minScore;
    long offsetStart;
    int count;
    long pk;

}
