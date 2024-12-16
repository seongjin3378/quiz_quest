package com.seong.portfolio.quiz_quest.webFlux.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
public class UserUsageTimeEmitImpl implements UserWebFluxService {
    private final Sinks.Many<String> userMulticastSink;

    @Override
    public void sendMessage(String message) {
        userMulticastSink.tryEmitNext(message);
    }

}
