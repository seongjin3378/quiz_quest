package com.seong.portfolio.quiz_quest.rankings.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;

public interface RedisRankingService {
    void initRankingRepository() throws JsonProcessingException;
    int updateUserUsageTime(int rankingScore, String rankingType) throws JsonProcessingException;
    int findRankingScore(RedisRankingVO redisRankingVO) throws JsonProcessingException;
}
