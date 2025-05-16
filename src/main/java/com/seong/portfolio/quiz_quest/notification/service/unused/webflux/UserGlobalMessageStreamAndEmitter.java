package com.seong.portfolio.quiz_quest.notification.service.unused.webflux;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("UserGlobalMessageStreamAndEmitter")
@Slf4j
@RequiredArgsConstructor
public class UserGlobalMessageStreamAndEmitter/* extends SinksManager implements StreamAndEmitter*/ {

   /* private final ObjectMapper objectMapper;



    @Override
    public Flux<ServerSentEvent<String>> streamSSE(String userId) {
        return null;
    }
    @Override
    public Flux<ServerSentEvent<String>> streamSSE() {

        log.info("streamSSE() 실행");

        return  getSink().asFlux()
                .map(messageJSON -> ServerSentEvent.<String> builder()
                        .event("message")
                        .data(messageJSON)
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
    public Sinks.EmitResult emit(Object message) throws JsonProcessingException {
        String messageJSON = objectMapper.writeValueAsString(message);
        return getSink().tryEmitNext(messageJSON);

     *//*   return getSinkByKey(messageVO.getReceiver().getUserId()).tryEmitNext(messageVO);*//*
    }*/
}
