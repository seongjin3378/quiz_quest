package com.seong.portfolio.quiz_quest.problems.enums;

import lombok.Getter;

@Getter
public enum ProblemType {
    DATA_STRUCTURE(1, "자료구조"),
    MATH(2, "수학"),
    SORTING(3, "정렬"),
    TREE(4, "트리"),
    ALGORITHM(5, "알고리즘"),
    SYNTAX(6, "문법");

    private final int index;
    private final String displayName;

    ProblemType(int index, String displayName) {
        this.index = index;
        this.displayName = displayName;
    }

    public static String getDisplayNameByIndex(int index) {
        for (ProblemType problemType : ProblemType.values()) {
            if (problemType.getIndex() == index) {
                return problemType.getDisplayName();
            }
        }
        return null; // 해당 인덱스가 없을 경우 null 반환
    }
}
