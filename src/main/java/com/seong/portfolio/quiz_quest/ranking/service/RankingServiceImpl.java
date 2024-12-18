package com.seong.portfolio.quiz_quest.ranking.service;


import com.seong.portfolio.quiz_quest.ranking.enums.RankingType;
import com.seong.portfolio.quiz_quest.ranking.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
    private final RankingRepository rankingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    private static final Logger logger = LoggerFactory.getLogger(RankingServiceImpl.class);

    @Override
    public int updateUserUsageTime(int rankingScore) {
        String userId = sessionService.getSessionId();
        Boolean isUserIdExists = redisTemplate.hasKey("JSESSIONID:" + userId);
        logger.info("userId: {}", userId);

        if (isUserIdExists && rankingScore > 0) {
            logger.info("함수 실행중");
            RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
            int prevRankingScore = rankingRepository.findRankingScore(vo);
            if(prevRankingScore + 60 == rankingScore) {
                vo.setRankingScore(rankingScore); // 분으로 저장할지 1.2 시간 처럼 저장할지 생각 중
                logger.info("rankingScore: {}", rankingScore);
                rankingRepository.updateRankingScore(vo);
                return 1;
            }
            else {
                logger.info("값이 조작되었습니다. {}", rankingScore);
                return prevRankingScore;
            }

        }
        return 0;
    }

    @Override
    public int findRankingScore(RankingVO vo) {
        return rankingRepository.findRankingScore(vo);
    }

    @Override
    public int saveOrUpdateRanking(RankingVO vo) {
        return rankingRepository.findRankingScore(vo);
    }

    @Override
    public void initializeRankingDB(RankingVO vo) {
        for(RankingType rankingType : RankingType.values()) {
            RankingVO rankingVO = RankingVO.builder().userId(sessionService.getSessionId()).rankingType(String.valueOf(rankingType.label())).rankingScore(0).rankingRank(-1).build();
            rankingRepository.save(rankingVO);
        }

    }
}

