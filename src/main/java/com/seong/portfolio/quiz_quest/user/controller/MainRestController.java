package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.user.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@RestController
@RequiredArgsConstructor
public class MainRestController {
    private final SessionService sessionService;
    private final RedisTemplate<String, Object> redisTemplate;
    Logger logger = LoggerFactory.getLogger(MainRestController.class);
    private final SessionRegistry sessionRegistry;

    @GetMapping("/loggedInUserCount")
    public ResponseEntity<Integer> loggedInUserCount() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");
        printAllSession();
        return ResponseEntity.ok(keys.size()); // HTTP 200 OK와 함께 사용자 수 반환
    }

    private void printAllSession()
    {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            logger.info("Principal: {}", principal);
            for (SessionInformation session : sessions) {
                logger.info("Session ID: {}", session.getSessionId());
                logger.info("Last Request: {}", session.getLastRequest());
                logger.info("Expiration Time: {}", session.getSessionId());
            }
        }
    }


    @PostMapping("/userConnectTimes/{userId}")
    public void userConnectTimes(@PathVariable String userId)
    {
    //만약 db에 유저 시간이 없을 경우

        String value = String.valueOf(redisTemplate.opsForValue().get(userId));

        if(value != null)
        {
            Duration duration = parseDuration(value);
            logger.info("duration Before: {}, {}, {}", duration.toHours(), duration.toMinutes(), duration.toSeconds());
            duration = duration.plusSeconds(1);
            redisTemplate.opsForValue().set(userId, duration);
            logger.info("duration After: {}, {}, {}", duration.toHours(), duration.toMinutes(), duration.toSeconds());
        }else{
            // LocalTime 객체 생성
            Duration duration = Duration.ofHours(0)
                    .plusMinutes(0)
                    .plusSeconds(0); // 60초는 1분으로 변환됨
            String formattedTime = formatDuration(duration);
            redisTemplate.opsForValue().set(userId, formattedTime);
            logger.info("duration 초기화: {}", redisTemplate.opsForValue().get(userId));

        }

    }



    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60);
        long seconds = (duration.getSeconds() % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Duration parseDuration(String timeString) {
        // "HH:mm:ss" 형식의 문자열을 ":"로
        String[] parts = timeString.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid time format. Expected format is HH:mm:ss");
        }

        // 각 부분을 정수로 변환
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);

        // Duration 객체 생성
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

}
