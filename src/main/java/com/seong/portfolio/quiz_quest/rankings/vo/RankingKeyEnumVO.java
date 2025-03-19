package com.seong.portfolio.quiz_quest.rankings.vo;

import com.seong.portfolio.quiz_quest.rankings.enums.RankingKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankingKeyEnumVO {
    private final RankingKey rankingKey;



    public static RankingKeyEnumVO fromString(String language) {
        return switch (language.toLowerCase()) {
            case "usage_time" -> new RankingKeyEnumVO(RankingKey.USAGE_TIME);
            case "total_problem" -> new RankingKeyEnumVO(RankingKey.TOTAL_PROBLEM);
            case "language" -> new RankingKeyEnumVO(RankingKey.LANGUAGE);
            default -> throw new IllegalArgumentException("해당 redis key 값이 없습니다.");
        };
    }

}
