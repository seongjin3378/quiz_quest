package com.seong.portfolio.quiz_quest.notification.repo;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface NotificationEmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void save(String emitterId, SseEmitter sseEmitter, boolean expired);

    SseEmitter findExpiredEmitterById(String emitterId);

    void saveEventCache(String emitterId, Object event);

    Object findEventCache(String emitterId);

    Map<String, SseEmitter> findAllExpiredEmitterStartWithByMemberId(String memberId);
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId);


    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) ;


    //삭제

     void deleteById(String emitterId) ;
     void deleteById(String emitterId, boolean expired);

    void deleteAllEmitterStartWithId(String memberId) ;


     void deleteAllEventCacheStartWithId(String memberId);

}
