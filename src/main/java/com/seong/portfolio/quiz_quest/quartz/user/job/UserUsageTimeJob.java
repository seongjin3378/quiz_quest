package com.seong.portfolio.quiz_quest.quartz.user.job;

import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class UserUsageTimeJob implements Job {
    private final RankingRepository rankingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserUsageTimeJob.class);

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("잡 실행");
        List<String> userIdList = userRepository.findAllUserId();
        Set<String> keys = redisTemplate.keys("JSESSIONID:*");

        /*
         접속중인 사용자는 db 접근 제외,
         사용중인 유저가 db 접근을 하는 로직이 있어, db락으로 충돌 가능성이 있음
        */

        for (String userId : userIdList) {
            if(!keys.contains("JSESSIONID:" + userId))
            {
                RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
                rankingRepository.saveOrUpdateRanking(vo);
            }
        }



    }
}
