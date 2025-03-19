package com.seong.portfolio.quiz_quest.quartz.rankings.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.quartz.interfaces.JobService;
import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.rankings.service.redis.RedisRankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import com.seong.portfolio.quiz_quest.user.service.user.ActiveUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Qualifier("UsageTimeUpdateJob")
@Slf4j
public class UsageTimeUpdateJobServiceImpl implements JobService {
    private final ActiveUserService activeUserService;
    private final RedisRankingService redisRankingService;
    private final RankingRepository rankingRepository;

    void updateRedisRankingScores()
    {
        activeUserService.findAll().forEach(userId -> {
            userId = userId.replace("JSESSIONID:", "");
            String key = RankingKeyEnumVO.fromString("usage_time").getRankingKey().getKey() + ":week";
            RedisRankingVO redisRankingVO = RedisRankingVO.builder().key(key).value(userId).build();
            int rankingScore;
            try {
                rankingScore = redisRankingService.findRankingScore(redisRankingVO);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
            RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").rankingScore(rankingScore).build();
            rankingRepository.updateRankingScore(vo);
        });
    }
    @Override
    public void execute() {
        updateRedisRankingScores();
    }
}
