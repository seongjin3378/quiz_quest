package com.seong.portfolio.quiz_quest.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seong.portfolio.quiz_quest.message.repo.MessageRepository;
import com.seong.portfolio.quiz_quest.message.service.MessageService;
import com.seong.portfolio.quiz_quest.message.vo.MessageVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final Map<String, Sinks.Many<MessageVO>> sinks = new ConcurrentHashMap<>();
    private final SessionService sessionService;
    private final MessageService messageService;

    @GetMapping(value = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamMessages(@PathVariable String userId) {
        if(sessionService.getSessionId().equals(userId)) {
            return messageService.stream(userId);
        }else{
            return Flux.empty();
        }

    }
    @GetMapping("/test/asyncDBTest")
    public Mono<Integer> asyncDBTest() {
        return null;
    }
    @PostMapping("/emit")
    public ResponseEntity<String> emitMessage(@RequestBody  MessageVO messageVO) {

        Sinks.EmitResult result = messageService.emit(messageVO);
        if (result.isSuccess()) {
            return ResponseEntity.ok("메시지 전송 성공: " + messageVO.getMessageContent());
        } else {
            return ResponseEntity.badRequest().body("메시지 전송 실패: " + result);
        }
    }
}
