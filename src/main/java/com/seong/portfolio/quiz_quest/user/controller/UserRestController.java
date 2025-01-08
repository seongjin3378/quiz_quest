package com.seong.portfolio.quiz_quest.user.controller;


import com.seong.portfolio.quiz_quest.user.service.SessionService;
import com.seong.portfolio.quiz_quest.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final RedisTemplate<String, Object> redisTemplate;
    Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @GetMapping("/loggedInUserCount")
    public ResponseEntity<Integer> loggedInUserCount() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");
        userService.deleteActiveUsers(keys);
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
        userService.setActiveUserWithExpiry();
        int userCount = userService.getAllActiveUsers();

        return ResponseEntity.ok(userCount);
    }

}
