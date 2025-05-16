package com.seong.portfolio.quiz_quest.notification.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.seong.portfolio.quiz_quest.notification.service.enums.NotificationType;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
    private long notificationId;
    private long receiverId;
    private String content;
    private NotificationType notificationType;
    private String url;
    private LocalDateTime sentDate;
    @JsonProperty("isNotice")
    private boolean isNotice;
    private UserVO sender;
    private UserVO receiver;


}
