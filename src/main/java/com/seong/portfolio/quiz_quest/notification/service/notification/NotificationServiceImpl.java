package com.seong.portfolio.quiz_quest.notification.service.notification;


import com.seong.portfolio.quiz_quest.notification.repo.NotificationEmitterRepository;
import com.seong.portfolio.quiz_quest.notification.repo.NotificationRepository;
import com.seong.portfolio.quiz_quest.notification.enums.NotificationType;
import com.seong.portfolio.quiz_quest.notification.vo.NotificationVO;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 / 6;
    private final NotificationEmitterRepository notificationEmitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter subscribe(String userNum,String lastEventId){
        // 고유한 아이디 생성
        String emitterId = userNum +"_" + System.currentTimeMillis();
        SseEmitter emitter  = notificationEmitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));


        //시간 초과나 비동기 요청이 안되면 자동으로 삭제
        emitter.onTimeout(() -> {
                log.info("타임아웃 실행");
                notificationEmitterRepository.save(emitterId, emitter, true);
                /*notificationEmitterRepository.deleteById(emitterId);*/
        });

        notificationEmitterRepository.save(emitterId, emitter);
        //최초 연결시 더미데이터가 없으면 503 오류가 발생하기 때문에 해당 더미 데이터 생성


        //lastEventId 있다는것은 연결이 종료됬다. 그래서 해당 데이터가 남아있는지 살펴보고 있다면 남은 데이터를 전송
        if(!lastEventId.isEmpty()){
            log.info("lastEvent 있음");
            resendCachedEventsAfterLastEventId(lastEventId, emitter, userNum);
            cleanupExpiredEmitters(userNum);

        }

        notificationEmitterRepository.deleteAllEventCacheStartWithId(String.valueOf(userNum));
        sendToClient(emitter,emitterId, "EventStream Created. [memberId=" + userNum + "]");

        return emitter;

    }

    public void send(long receiverId, NotificationType notificationType, String url, String content) {
        //long receiverId, NotificationType notificationType, String url, String content
        NotificationVO notificationVO = createNotification(receiverId, notificationType, url, content);
        notificationRepository.save(notificationVO);

        String memberId = String.valueOf(receiverId);
        log.info("member Id: {} ", memberId);
        Map<String, SseEmitter> sseEmitters = notificationEmitterRepository.findAllEmitterStartWithByMemberId(memberId);
        String eventId = memberId +"_"+System.currentTimeMillis();
        log.info("Elements Empty send Time : {}", memberId +"_"+System.currentTimeMillis());
        sseEmitters.forEach(
                (key, emitter) -> {
                    log.info("sseEmitter for each event cache key: {}, ", key);

                    boolean isExpiredEmitter = Objects.nonNull(notificationEmitterRepository.findExpiredEmitterById(key));
                    if(isExpiredEmitter) {
                        log.info("emitter 삭제");
                        notificationEmitterRepository.saveEventCache(eventId, notificationVO);
                    }
                    else {
                        sendToClient(emitter, key, notificationVO);

                    }
                });

    }



    private void resendCachedEventsAfterLastEventId(String lastEventId, SseEmitter emitter, String userNum)
    {
        Map<String, Object> events = notificationEmitterRepository.findAllEventCacheStartWithByMemberId(userNum);

        events.entrySet().stream().filter(entry -> lastEventId.compareTo(entry.getKey())<0)
                .forEach(entry ->
                        {

                            log.info("event 실행: {}", entry.getKey());
                            sendToClient(emitter,entry.getKey(),entry.getValue());

                        }
                );
    }

    private void cleanupExpiredEmitters(String userNum)
    {
        Map<String, SseEmitter> expiredEvents = notificationEmitterRepository.findAllExpiredEmitterStartWithByMemberId(String.valueOf(userNum));

        expiredEvents.forEach((key, value) ->
        {
            log.info("expired key 삭제");
            notificationEmitterRepository.deleteById(key);
            notificationEmitterRepository.deleteById(key, true);

        });
    }

    private NotificationVO createNotification(long receiverId, NotificationType notificationType, String url, String content) {
        return  NotificationVO.builder().receiverId(receiverId).notificationType(notificationType).content(content).url(url).build();
    }

     private void sendToClient(SseEmitter emitter, String emitterId, Object vo) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId) //lastEventId
                    .data(vo));
        } catch (IOException exception) {
            notificationEmitterRepository.deleteById(emitterId);
            log.error(exception.getMessage());
        }
    }

}
