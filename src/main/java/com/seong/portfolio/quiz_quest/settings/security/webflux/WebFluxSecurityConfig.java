package com.seong.portfolio.quiz_quest.settings.security.webflux;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;


public class WebFluxSecurityConfig {

    /*@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated() // 모든 요청 인증 필요
                )
                .httpBasic(Customizer.withDefaults()) // HTTP 기본 인증 활성화 (기본 설정 사용)
                .formLogin(Customizer.withDefaults()); // 폼 기반 인증 활성화 (기본 설정 사용)
        return http.build();
    }*/
}
