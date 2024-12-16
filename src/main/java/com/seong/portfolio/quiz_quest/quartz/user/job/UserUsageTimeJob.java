package com.seong.portfolio.quiz_quest.quartz.user.job;

import com.seong.portfolio.quiz_quest.ranking.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Set;

@RequiredArgsConstructor
public class UserUsageTimeJob implements Job {
    private final RankingRepository rankingRepository;
    private final SessionService sessionService;
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");
        if(!keys.isEmpty()) {
            for(String key: keys)
            {
                String userId = (String) redisTemplate.opsForValue().get(key);
                RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
                int rankingScore = rankingRepository.findRankingScore(vo);
                vo.setRankingScore(rankingScore+1);
                rankingRepository.updateRankingScore(vo);
            }
        }

         */

    }
}
