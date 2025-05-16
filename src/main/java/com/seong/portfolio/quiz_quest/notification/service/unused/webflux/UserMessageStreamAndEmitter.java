package com.seong.portfolio.quiz_quest.notification.service.unused.webflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Qualifier("UserMessageStreamAndEmitter")
@Slf4j
public class UserMessageStreamAndEmitter/* extends SinksManager implements StreamAndEmitter*/ {
   /* private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public Flux<ServerSentEvent<String>> streamSSE(String userId)
    {

        return  getSinkByKey(userId).asFlux()
                .map(messageJson -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data(String.valueOf(messageJson))
                        .build())
                .onErrorResume(e -> {
                    log.error("Error in SSE stream: {}", e.getMessage());
                    return Flux.just(ServerSentEvent.<String> builder()
                            .event("error")
                            .data("{\"error\":\"" + e.getMessage() + "\"}")
                            .build());
                });
    }
    
    @Override
    public Flux<ServerSentEvent<String>> streamSSE() {

        return null;
    }

    @Override
    public Sinks.EmitResult emit(Object vo) throws JsonProcessingException {
        MessageVO messageVO = (MessageVO) vo;
        String messageVoString = objectMapper.writeValueAsString(messageVO);
        return getSinkByKey(messageVO.getReceiver().getUserId()).tryEmitNext(messageVoString);
    }
    */

}
