package com.seong.portfolio.quiz_quest.notification.service.notification;

import com.seong.portfolio.quiz_quest.notification.enums.NotificationType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String userNum, String lastEventId);

    void send(long receiverId, NotificationType notificationType, String url, String content);



}