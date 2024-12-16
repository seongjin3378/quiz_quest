package com.seong.portfolio.quiz_quest.user.controller;

import com.seong.portfolio.quiz_quest.user.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionController {
    private final static Logger logger = LoggerFactory.getLogger(SessionController.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    @PostMapping("/invalidateUser")
    public void invalidateUser(HttpSession session) {
        String key = "JSESSIONID:" + sessionService.getSessionId();
        redisTemplate.delete(key);
        logger.info("invalidate User ");
    }

}
