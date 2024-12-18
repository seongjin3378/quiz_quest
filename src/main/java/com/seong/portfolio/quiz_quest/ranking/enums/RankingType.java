package com.seong.portfolio.quiz_quest.ranking.enums;

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
}
