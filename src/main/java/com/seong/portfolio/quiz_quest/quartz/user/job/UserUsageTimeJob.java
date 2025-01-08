package com.seong.portfolio.quiz_quest.quartz.user.job;

import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@RequiredArgsConstructor
public class UserUsageTimeJob implements Job {
    private final RankingRepository rankingRepository;
    private final SessionService sessionService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserUsageTimeJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("잡 실행");
        List<String> userIdList = userRepository.findAllUserId();
        for (String userId : userIdList) {
            RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
            rankingRepository.saveOrUpdateRanking(vo);
        }



    }
}
