package com.seong.portfolio.quiz_quest.message.vo;


import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
    private long messageId;
    private long senderId;
    private long receiverId;
    private String messageContent;
    private LocalDateTime sentDate;
    private boolean isNotice;
    private UserVO sender;
    private UserVO receiver;
}
