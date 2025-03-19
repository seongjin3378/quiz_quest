package com.seong.portfolio.quiz_quest.rankings.enums;

public enum RankingType {
    USAGE_TIME("usage_time"),
    TOTAL_PROBLEM("total_problem"),
    LANGUAGE("language");

    private final String label;
    
    RankingType(String label) {
        this.label = label;
    }

    public String label()
    {
        return label;
    }

    public static String[] getLabels() {
        return java.util.Arrays.stream(RankingType.values())
                .map(RankingType::label)  // 각 enum의 label() 호출
                .toArray(String[]::new);  // String 배열로 변환
    }
}
