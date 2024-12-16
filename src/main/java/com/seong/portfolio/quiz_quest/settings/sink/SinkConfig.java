package com.seong.portfolio.quiz_quest.settings.sink;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig<String> {
    @Bean
    public Sinks.Many<String> userMulticastSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

}
