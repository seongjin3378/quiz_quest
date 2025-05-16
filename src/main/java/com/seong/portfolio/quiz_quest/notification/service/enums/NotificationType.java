package com.seong.portfolio.quiz_quest.notification.service.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    /**
     * 좋아요 알림 타입
     */
    LIKE("like"),

    /**
     * 댓글 달림 알림 타입
     */
    COMMENT("comment");

    /**
     * -- GETTER --
     *  알림 타입에 대한 설명 반환
     *
     * @return 설명 문자열
     */
    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

}
