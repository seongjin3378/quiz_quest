package com.seong.portfolio.quiz_quest.notification.controller;


import com.seong.portfolio.quiz_quest.notification.enums.NotificationType;
import com.seong.portfolio.quiz_quest.notification.service.notification.NotificationService;
import com.seong.portfolio.quiz_quest.notification.vo.NotificationVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Tag(name = "메시지 API", description = "알림 및 사용자 메시지 API")
@Slf4j
public class NotificationRestController {

    private final NotificationService notificationService;
    private final SessionService sessionService;

    @GetMapping(value= "/subscribe/{userNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String userNum,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId)  {
        log.info("{} emitter Subscribe", userNum);

        log.info("Last Event ID: {}", lastEventId);
        return ResponseEntity.ok(notificationService.subscribe(userNum, lastEventId));
    }
    

    @PostMapping(value= "/send/{receiverNum}")
    public ResponseEntity<String> send(@PathVariable long receiverNum, @RequestBody NotificationVO notificationVO) {
        log.info("{} emitter Notify", receiverNum);


        if(notificationVO.getNotificationType().equals(NotificationType.COMMENT)) {
            String sender = sessionService.getSessionId();

            //long receiverId, NotificationType notificationType, String url, String content
            notificationService.send(receiverNum, NotificationType.COMMENT, notificationVO.getUrl(), sender+"님이 댓글을 달았습니다!");
        }

        return ResponseEntity.ok("notification success");
    }
}
