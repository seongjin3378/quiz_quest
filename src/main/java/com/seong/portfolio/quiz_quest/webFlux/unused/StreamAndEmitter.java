package com.seong.portfolio.quiz_quest.webFlux.unused;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public interface StreamAndEmitter {

  /*  Flux<ServerSentEvent<String>> streamSSE(String key);

    Flux<ServerSentEvent<String>> streamSSE();

    Sinks.EmitResult emit(Object vo) throws JsonProcessingException;
*/
}