package com.seong.portfolio.quiz_quest.rankings.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.repo.InitializeRedisRankingRepository;
import com.seong.portfolio.quiz_quest.rankings.repo.RedisRankingRepository;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRankingServiceImpl implements RedisRankingService {
    private final InitializeRedisRankingRepository initializeRedisRankingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    private final RedisRankingRepository redisRankingRepository;
   @PostConstruct
    public void init() throws JsonProcessingException {
        Set<String> keys = redisTemplate.keys("ranking:*");

        if(keys.isEmpty()) {
            initRankingRepository();
            log.info("Initialize ranking repository successfully");
        }

    }

    public void initRankingRepository() throws JsonProcessingException {
        for(String timeUnit : new String[] {"week", "month", "year"}) {
            initializeRedisRankingRepository.execute("usage_time", timeUnit);
            initializeRedisRankingRepository.execute("total_problem", timeUnit);
            initializeRedisRankingRepository.execute("language", timeUnit);
        }
    }

    @Override
    @Transactional
    public int updateUserUsageTime(int rankingScore, String rankingType) throws JsonProcessingException {
        String userId = sessionService.getSessionId();
        Boolean isUserIdExists = redisTemplate.hasKey("JSESSIONID:" + userId);
        log.info("userId: {}, isUserIdExists: {}", userId, isUserIdExists);


        if (isUserIdExists && rankingScore > 0) {
            log.info("함수 실행중");
            String key = RankingKeyEnumVO.fromString(rankingType).getRankingKey().getKey() + ":week";
            RedisRankingVO vo = RedisRankingVO.builder().value(userId).key(key).build();
            int prevRankingScore = redisRankingRepository.findRankingScoreByUserIdAndRankingType(vo);
            return validateAndSaveOrReturnPreviousScore(prevRankingScore, rankingScore, vo);

        }
        log.info("0 출력");
        return 0;
    }

    public int validateAndSaveOrReturnPreviousScore(int prevRankingScore, int rankingScore, RedisRankingVO vo) throws JsonProcessingException {
        if(prevRankingScore + 60 == rankingScore) {
            log.info("rankingScore: {}", rankingScore);
            redisRankingRepository.save(vo, rankingScore);
            return 1;
        }
        else {
            log.info("값이 조작되었습니다. {}", rankingScore);
            return prevRankingScore;
        }
    }

    @Override
    public int findRankingScore(RedisRankingVO redisRankingVO) throws JsonProcessingException {
        return redisRankingRepository.findRankingScoreByUserIdAndRankingType(redisRankingVO);
    }

}
