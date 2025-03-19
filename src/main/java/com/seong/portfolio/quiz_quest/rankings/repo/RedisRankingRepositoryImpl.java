package com.seong.portfolio.quiz_quest.rankings.repo;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisRankingRepositoryImpl implements RedisRankingRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RankingRepository rankingRepository;

    @Override
    public void add(RedisRankingVO redisRankingVO, int score) throws JsonProcessingException {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        String value = redisRankingVO.getValue();
        zSetOps.incrementScore(redisRankingVO.getKey(), value, score);
    }

    @Override
    public void save(RedisRankingVO redisRankingVO, int score) throws JsonProcessingException {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        String value = redisRankingVO.getValue();
        zSetOps.add(redisRankingVO.getKey(), value, score);
    }


    public Set<ZSetOperations.TypedTuple<Object>> findAll(String key) throws IOException {
        Set<ZSetOperations.TypedTuple<Object>> resultTuple = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        assert resultTuple != null;

        return resultTuple;
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> findAllByTimeUnitOrderByScoreDesc(RedisRankingVO redisRankingVO, String timeUnitType) throws IOException {
        String key = redisRankingVO.getKey()+":"+timeUnitType;
        log.info(key);
        Set<ZSetOperations.TypedTuple<Object>> resultTuple = redisTemplate.opsForZSet().reverseRangeWithScores(key, redisRankingVO.getOffsetStart(), redisRankingVO.getCount());
        log.info("rankingPkSet: {}", resultTuple);
        assert resultTuple != null;
        return resultTuple;
    }

    @Override
    public int findRankingScoreByUserIdAndRankingType(RedisRankingVO redisRankingVO) {
        String key = redisRankingVO.getKey();

        Double score = redisTemplate.opsForZSet().score(key, redisRankingVO.getValue());
        if(score == null) {
            redisTemplate.opsForZSet().add(key, redisRankingVO.getValue(), 0);
            score = (double) 0;
        }
        return (int) Math.round(score);

    }

    @Override
    public void removeAllValues(String key) {
        redisTemplate.opsForZSet().removeRange(key, 0, -1);
    }

    @Override
    public void updateAllScoresByTimeUnitFromDB(String rankingType, String key) throws IOException {

            // Redis에서 모든 점수를 가져옴
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = findAll(key);

            // 점수 업데이트
            for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                String userId = Objects.requireNonNull(typedTuple.getValue()).toString();
                int score = (int) Math.floor(Objects.requireNonNull(typedTuple.getScore()));
                log.info("score: {}, userId: {}", score, userId);
                rankingRepository.updateRankingScore(RankingVO.builder()
                        .rankingType(rankingType)
                        .rankingScore(score)
                        .userId(userId)
                        .build());
            }
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Object>> findByUserId(String userId) {
        return Set.of();
    }
}
