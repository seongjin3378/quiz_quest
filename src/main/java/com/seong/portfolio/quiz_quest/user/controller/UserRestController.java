package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.user.service.user.ActiveUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final RedisTemplate<String, Object> redisTemplate;
    Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final ActiveUserService activeUserService;

    @GetMapping("/loggedInUserCount")
    public ResponseEntity<Integer> loggedInUserCount() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");
        activeUserService.delete(keys);
        printAllJSessionIds();
        return ResponseEntity.ok(keys.size()); // HTTP 200 OK와 함께 사용자 수 반환
    }

    private void printAllJSessionIds() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");

        if (!keys.isEmpty()) {
            for (String key : keys) {
                logger.info("Found key: {}", key);
            }
        } else {
            logger.info("No keys found for pattern 'JSESSIONID:*'");
        }
    }


    @PostMapping("/current")
    public ResponseEntity<Integer> setActiveUser() {
        activeUserService.setIdWithExpiry();
        int userCount = activeUserService.getAllCount();

        return ResponseEntity.ok(userCount);
    }

}
