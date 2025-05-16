package com.seong.portfolio.quiz_quest.webFlux.unused;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;


@Slf4j
public class SerializedDataEmitter {

   /* public static void run(StreamAndEmitter emitter, String message)
    {
        Sinks.EmitResult result = null;
        try {
            result = emitter.emit(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (result.isFailure()) {
            // emit 실패 처리 로직 (예: 로그 출력, 재시도 등)
            log.error("Failed to emit message: {}", result);

        }
    }*/
}
