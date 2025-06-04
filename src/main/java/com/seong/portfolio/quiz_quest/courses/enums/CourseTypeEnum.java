package com.seong.portfolio.quiz_quest.courses.enums;

import com.seong.portfolio.quiz_quest.problems.enums.ProblemType;
import lombok.Getter;

@Getter
public enum CourseTypeEnum {
        BACKEND   (1, "백엔드"),
        FRONTEND  (2, "프론트엔드"),
        DATABASE  (3, "데이터베이스"),
        DEVOPS    (4, "데브옵스"),
        MOBILE    (5, "모바일"),
        FULLSTACK (6, "풀스택"),
        ETC       (7, "기타");


        private final int index;
        private final String displayName;

        CourseTypeEnum(int index, String displayName) {
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
