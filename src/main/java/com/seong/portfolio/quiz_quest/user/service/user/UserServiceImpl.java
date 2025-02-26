package com.seong.portfolio.quiz_quest.user.service.user;

import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    @Override
    public void joinProcess(UserVO vo) {
        int isUser = userRepository.existsByUserId(vo);
        if(isUser == 1) {
            return;
        }
        userRepository.save(vo);
    }

    @Override
    public int getAllActiveUsers() {
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");

        return keys.size();
    }

    @Override
    public void setActiveUserWithExpiry() {
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
    public void deleteActiveUsers(Set<String> keys ) {
        for (String key : keys) {
            // TTL 값을 확인합니다.
            Long ttl = redisTemplate.getExpire(key);
            // TTL이 음수인 경우 해당 키를 삭제
            if (ttl < 0) {
                redisTemplate.delete(key);
            }
        }
    }


}
