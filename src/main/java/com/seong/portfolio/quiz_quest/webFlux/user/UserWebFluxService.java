package com.seong.portfolio.quiz_quest.webFlux.user;

import reactor.core.publisher.Flux;

public interface UserWebFluxService {
    void sendMessage(String message);
}
