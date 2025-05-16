package com.seong.portfolio.quiz_quest.notification.controller.unused;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MessageRestTestController {
   /* private final SessionService sessionService;


    private final MessageService messageService;


    public MessageRestController(SessionService sessionService,  MessageService messageService, ObjectMapper objectMapper) {
        this.sessionService = sessionService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/{userId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> userStreamMessages(@PathVariable String userId) {
        if (sessionService.getSessionId().equals(userId)) {

            return userMessageStreamAndEmitter.streamSSE(userId);
        } else {
            return Flux.empty();
        }

    }

    @PostMapping("/emit")
    public Mono<String> emitMessage(@RequestBody MessageVO messageVO) throws JsonProcessingException {

        return messageService.saveMessage(messageVO).doOnSuccess(messageJson -> {
            SerializedDataEmitter.run(userGlobalMessageStreamAndEmitter, String.valueOf(messageJson));
        });
    }


    @GetMapping(value = "/global/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> globalUserStreamMessages() {
        return userGlobalMessageStreamAndEmitter.streamSSE();
    }

    @PostMapping("/global/emit")
    public Mono<String> globalEmitMessage(@RequestBody MessageVO messageVO) {

        log.info(messageVO.toString());


        return messageService.saveMessage(messageVO).doOnSuccess(messageJson -> {
          SerializedDataEmitter.run(userGlobalMessageStreamAndEmitter, messageJson);
        });
        // 3. DB 저장 결과를 컨트롤러의 응답으로 반환합니다.
        // 또는 Mono<Void>를 반환하려면 .then()을 사용할 수 있습니다.
        // .then(); // 응답 본문 없이 작업 완료만 알림

    }






*//*    @Operation(summary = "모든 사용자를 위한 스트림", description = "모든 사용자 스트림에게 어드민이 메시지를 보냄")
    @GetMapping(value = "/api/v1/message/stream/global-user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> globalUserStreamMessage() {

    }*//*

    @GetMapping("/test/asyncDBTest")
    public Mono<Integer> asyncDBTest() {
        return null;
    }*/

}
