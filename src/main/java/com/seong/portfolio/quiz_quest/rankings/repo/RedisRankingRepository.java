package com.seong.portfolio.quiz_quest.rankings.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.vo.RedisRankingVO;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.IOException;
import java.util.Set;


/*- rankings/repo/redisRankingRepository
    - 순위를 표시하기 위해 데이터 베이스에서 값을 불러와 레디스에 저장 및 불러오는 역할
    - 레디스에서 SortedSet 을 이용하여 내림차순으로 불러옴
      - 값을 넣을 때는 ZsetOperation을 이용
      - 불러올때는 opsForZSet().reverseRangeByScore 을 이용
    - value 값은 객체로 넣을 수 없으니 ObjectMapper을 이용하여 Json으로 저장, 불러올때는 반대로 객체로 파싱
* */

public interface RedisRankingRepository {
    void add(RedisRankingVO redisRankingVO, int score) throws JsonProcessingException;
    void save(RedisRankingVO redisRankingVO, int score) throws JsonProcessingException;
    Set<ZSetOperations.TypedTuple<Object>> findAll(String key) throws IOException;
    Set<ZSetOperations.TypedTuple<Object>> findByUserId(String userId);
    Set<ZSetOperations.TypedTuple<Object>> findAllByTimeUnitOrderByScoreDesc(RedisRankingVO redisRankingVO, String timeUnitType) throws IOException;
    int findRankingScoreByUserIdAndRankingType(RedisRankingVO redisRankingVO);
    void removeAllValues(String key);
    void updateAllScoresByTimeUnitFromDB(String rankingType, String key) throws IOException;
}
