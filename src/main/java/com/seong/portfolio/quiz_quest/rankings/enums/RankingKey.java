package com.seong.portfolio.quiz_quest.rankings.enums;

import java.util.Arrays;

public enum RankingKey {
    USAGE_TIME("ranking:usageTime"),
    TOTAL_PROBLEM("ranking:totalProblem"),
    LANGUAGE("ranking:language");

    private final String rankingKey;
    RankingKey(String rankingKey) {this.rankingKey = rankingKey;}

    public String getKey() {return rankingKey;}

    public static String[] getKeys() {
        return Arrays.stream(RankingKey.values())
                .map(RankingKey::getKey)  // 각 enum의 getKey() 호출
                .toArray(String[]::new);  // String 배열로 변환
    }
}
