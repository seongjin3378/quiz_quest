package com.seong.portfolio.quiz_quest.rankings.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.service.ranking.RankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingKeyEnumVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import com.seong.portfolio.quiz_quest.utils.date.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InitializeRedisRankingRepository {
    private final RedisRankingRepository redisRankingRepository;
    private final RankingService rankingService;
    public void execute(String rankingType, String timeUnitType) throws JsonProcessingException {
        List<RankingVO> vo = rankingService.findAllByRankingType(rankingType);

        for(RankingVO item : vo) {
            if(!isCreatedAtValidForTimeUnit(timeUnitType, item.getCreatedAt()) || item.getRankingScore() == 0) {
                continue; //item이 timeUnitType과 불일치 할 경우 다음 item 조회
            }
            String key = RankingKeyEnumVO.fromString(rankingType).getRankingKey().getKey() +":"+timeUnitType;
            redisRankingRepository.add(RedisRankingVO.builder()
                                  .value(item.getUserId())
                                  .key(key)
                                  .build(), item.getRankingScore());

        }
    }

    private boolean isCreatedAtValidForTimeUnit(String timeUnitType, LocalDateTime createdAt) {

        return switch (timeUnitType) {
            case "week" -> DateUtils.isThisWeek(createdAt);
            case "month" -> DateUtils.isThisMonth(createdAt);
            case "year" -> DateUtils.isThisYear(createdAt);
            default -> throw new IllegalArgumentException("Invalid time unit type");
        };

    }

}
