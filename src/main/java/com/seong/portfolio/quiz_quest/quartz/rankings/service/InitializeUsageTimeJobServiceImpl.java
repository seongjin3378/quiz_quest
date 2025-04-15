package com.seong.portfolio.quiz_quest.quartz.rankings.service;


import com.seong.portfolio.quiz_quest.quartz.interfaces.JobService;
import com.seong.portfolio.quiz_quest.rankings.enums.RankingType;
import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.rankings.repo.RedisRankingRepository;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.active.ActiveUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Qualifier("InitializeUsageTimeJob")
@Slf4j
public class InitializeUsageTimeJobServiceImpl implements JobService {
    private final ActiveUserService activeUserService;
    private final RankingRepository rankingRepository;
    private final RedisRankingRepository redisRankingRepository;


    public void createRankingRecordForInActiveUsers()
    {
        Set<String> userIds = activeUserService.findAllInactiveUsers();

        for(String userId : userIds) {
            RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
            rankingRepository.saveOrUpdateRanking(vo); // 레코드 생성
        }
    }



    public void updateScoresAndInitializeRedisRankingForWeek()
    {
        Arrays.stream(RankingType.getLabels()).forEach(rankingType -> {
                try {
                    String key = RankingKeyEnumVO.fromString(rankingType).getRankingKey().getKey() + ":week";
                    redisRankingRepository.updateAllScoresByTimeUnitFromDB(rankingType, key);
                    redisRankingRepository.removeAllValues(key);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

    }
    
    //만들 것 있음


    @Override
    @Transactional
    public void execute() {
        log.info("실행");
        updateScoresAndInitializeRedisRankingForWeek();
        createRankingRecordForInActiveUsers();


    }
}
