package com.seong.portfolio.quiz_quest.rankings.service;


import com.seong.portfolio.quiz_quest.rankings.enums.RankingType;
import com.seong.portfolio.quiz_quest.rankings.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
    private final RankingRepository rankingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SessionService sessionService;
    private static final Logger logger = LoggerFactory.getLogger(RankingServiceImpl.class);

    @Override
    @Transactional
    public int updateUserUsageTime(int rankingScore) {
        String userId = sessionService.getSessionId();
        Boolean isUserIdExists = redisTemplate.hasKey("JSESSIONID:" + userId);
        logger.info("userId: {}, isUserIdExists: {}", userId, isUserIdExists);


        if (isUserIdExists && rankingScore > 0) {
            logger.info("함수 실행중");
            RankingVO vo = RankingVO.builder().userId(userId).rankingType("usage_time").build();
            int prevRankingScore = findRankingScore(vo);
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
        logger.info("0 출력");
        return 0;
    }

    @Override
    public int findRankingScore(RankingVO vo) {
        int rankingScore = rankingRepository.findRankingScore(vo);
        String userId = sessionService.getSessionId();

        if(rankingScore == -1) { // 랭킹 스코어를 찾지 못할 시 DB 초기화
            initializeRankingDB(UserVO.builder().userId(userId).build());
        }
        rankingScore = rankingRepository.findRankingScore(vo);
        return rankingScore;
    }

    @Override
    public int saveOrUpdateRanking(RankingVO vo) {
        return rankingRepository.findRankingScore(vo);
    }

    @Override
    public void initializeRankingDB(UserVO vo) {
        for(RankingType rankingType : RankingType.values()) {
            RankingVO rankingVO = RankingVO.builder().userId(vo.getUserId()).rankingType(String.valueOf(rankingType.label())).rankingScore(0).rankingRank(-1).build();
            rankingRepository.save(rankingVO);
        }

    }
}

