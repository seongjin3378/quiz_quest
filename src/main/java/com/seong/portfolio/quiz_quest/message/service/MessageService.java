package com.seong.portfolio.quiz_quest.message.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.protocol.Message;
import com.seong.portfolio.quiz_quest.message.repo.MessageAsyncRepository;
import com.seong.portfolio.quiz_quest.message.repo.MessageRepository;
import com.seong.portfolio.quiz_quest.message.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageAsyncRepository messageAsyncRepository;
    private final Map<String, Sinks.Many<MessageVO>> sinks = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private Sinks.Many<MessageVO> getSink(String userId) {
        return sinks.computeIfAbsent(userId, this::createSink);
    }
    private Sinks.Many<MessageVO> createSink(String userId) {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    private Flux<String> saveMessage(MessageVO messageVO)  {

        log.info("start");
        messageAsyncRepository.save(messageVO);
        log.info("end");
        String result = null;
        try {
            result = objectMapper.writeValueAsString(messageVO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Flux.just(result);
    }




    public Flux<ServerSentEvent<String>> stream(String userId)
    {

        return  getSink(userId).asFlux()
                .flatMap(this::saveMessage)
                .map(messageVO -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(messageVO)
                        .build());

    }

    public Sinks.EmitResult emit(MessageVO messageVO)
    {
        return getSink(messageVO.getReceiver().getUserId()).tryEmitNext(messageVO);
    }

}
