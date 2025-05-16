package com.seong.portfolio.quiz_quest.notification.repo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class NotificationEmitterRepositoryImpl implements NotificationEmitterRepository {


        private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
        private final Map<String, SseEmitter> expiredEmitters = new ConcurrentHashMap<>();

        private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

        @Override
        public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
            emitters.put(emitterId,sseEmitter);
            return sseEmitter;
        }

        public void save(String emitterId, SseEmitter sseEmitter, boolean expired) {
            expiredEmitters.put(emitterId,sseEmitter);
        }

    @Override
    public SseEmitter findExpiredEmitterById(String emitterId) {

            return expiredEmitters.get(emitterId);
    }

    @Override
    public Map<String, SseEmitter> findAllExpiredEmitterStartWithByMemberId(String memberId) {

        expiredEmitters.forEach((key, value) -> {
            log.info("Emitter Key: {}, Value: {}", key, value);
        });

        return expiredEmitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Override
        public void saveEventCache(String emitterId, Object event) {
            eventCache.put(emitterId,event);
        }

    @Override
    public Object findEventCache(String emitterId) {
        return eventCache.get(emitterId);
    }

    @Override
        public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {

            emitters.forEach((key, value) -> {
                log.info("Emitter Key: {}, Value: {}", key, value);
            });

            return emitters.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(memberId))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        @Override
        public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {


            return eventCache.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(memberId))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        //삭제
        @Override
        public void deleteById(String emitterId) {
            emitters.remove(emitterId);
        }

        @Override
        public void deleteById(String emitterId, boolean expired) {
            expiredEmitters.remove(emitterId);
        }


        @Override
        public void deleteAllEmitterStartWithId(String memberId) {
            emitters.forEach(
                    (key,emitter) -> {
                        if(key.startsWith(memberId)){
                            emitters.remove(key);
                        }
                    }
            );
        }

        @Override
        public void deleteAllEventCacheStartWithId(String memberId) {
            eventCache.forEach(
                    (key,value) -> {
                        if(key.startsWith(memberId)){
                            eventCache.remove(key);
                        }
                    }
            );
        }
}
