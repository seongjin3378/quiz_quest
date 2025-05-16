package com.seong.portfolio.quiz_quest.webFlux.unused;

import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SinksManager {
/*    private final Map<String, Sinks.Many<String>> sinks = new ConcurrentHashMap<>();
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    public Sinks.Many<String> getSinkByKey(String key) {
        return sinks.computeIfAbsent(key, this::createSink);
    }

*//*    public Flux<ServerSentEvent<String>> returnStreamByKey(String key, Function<K, Flux<String>> fluxFunc) {
        return getSinkByKey(key).asFlux()
                .flatMap(fluxFunc)
                .map(vo -> ServerSentEvent.<String>builder()
                        .event("message")
                        .data( vo)
                        .build());
    }*//*

    private Sinks.Many<String> createSink(String key) {
        return Sinks.many().multicast().onBackpressureBuffer();
    }


*//*    public Flux<ServerSentEvent<String>> returnStream(Function<K, Flux<String>> fluxFunc)
    {
        return createSink().asFlux().map(json -> ServerSentEvent.<String>builder()
                .event("message")
                .data((String) json)
                .build());
    }*//*

    public Sinks.Many<String> getSink() {
        return sink;
    }*/

}
