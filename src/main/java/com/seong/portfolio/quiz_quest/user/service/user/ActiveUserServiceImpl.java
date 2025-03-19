package com.seong.portfolio.quiz_quest.user.service.user;

import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActiveUserServiceImpl implements ActiveUserService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    @Override
    public int getAllCount() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");

        return keys.size();
    }

    @Override
    public void setIdWithExpiry() {
        String userId = sessionService.getSessionId();
        log.info("userId: {}", userId);
        String key = "JSESSIONID:" + userId;


        if(!userId.equals("Anonymous") ) {

            redisTemplate.opsForValue().set(key, "");
            log.info("세션 생성: {}, Redis에 저장된 활성 세션 수: {}", userId, redisTemplate.opsForValue().get(key));
            redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        }
    }

    @Override
    public void delete(Set<String> keys ) {
        for (String key : keys) {
            // TTL 값을 확인합니다.
            Long ttl = redisTemplate.getExpire(key);
            // TTL이 음수인 경우 해당 키를 삭제
            if (ttl < 0) {
                redisTemplate.delete(key);
            }
        }
    }

    @Override
    public Set<String> findAll() {
        return redisTemplate.keys("JSESSIONID:*");
    }

    @Override
    public Set<String> findAllInactiveUsers() {
        List<String> userIdList = userRepository.findAllUserId();
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");
        Set<String> result = new HashSet<>();
        for (String userId : userIdList) {
            if (!keys.contains("JSESSIONID:" + userId)) {
                result.add(userId);
            }
        }
        return result;
    }
}
