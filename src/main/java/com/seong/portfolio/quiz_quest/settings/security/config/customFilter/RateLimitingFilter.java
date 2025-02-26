package com.seong.portfolio.quiz_quest.settings.security.config.customFilter;

import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/*
api: /saveUsageTimer
조작된 요청을 막기위해 1분에 1번씩만 요청하도록 하는 커스텀 필터
* */



@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final ExpiringMap<String, Bucket> buckets = ExpiringMap.builder()
            .expiration(1, TimeUnit.MINUTES).build();
    private final ApplicationContext applicationContext;
    private final SessionService sessionService;

    private final static Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    @Autowired
    public RateLimitingFilter(ApplicationContext applicationContext, SessionService sessionService) {
        this.applicationContext = applicationContext;
        this.sessionService = sessionService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("requestURI {}", request.getRequestURI());

        if ("/saveUsageTimer".equals(request.getRequestURI())) {

            if ("POST".equalsIgnoreCase(request.getMethod())) {

                logger.info("내부 동작 실행");
                String userId = sessionService.getSessionId();
                /*userId 대한 버킷 검색 만약 key 값이 없으면 Scope Bucket 빈 생성해서 맵에 넣음*/
                Bucket bucket = buckets.computeIfAbsent(userId, k -> applicationContext.getBean("saveUsageTimerBucket", Bucket.class));
                if (bucket.tryConsume(1)) {
                } else {/*버킷 설정한 요청 횟수보다 많은 경우 오류 출력*/
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    return;
                }

            }

        }

        filterChain.doFilter(request, response);
    }
}

