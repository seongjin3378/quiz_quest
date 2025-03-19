package com.seong.portfolio.quiz_quest.rankings.repo;


import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/*
ranking Type
- usage_time : 사용자들의 게시판 이용 시간을 랭킹 순으로 보여줌 (주, 월 단위)
- total_problem : 총 문제를 맞춘 횟수를 랭킹 순으로 보여줌
- language : 언어 별로 문제를 많이
*/

/* findTopNByOrderByRankingScoreDesc*/
/*  RankingScore가 높은 순으로 내림차순으로 n개의 데이터를 가져옴 */


/*saveOrUpdateRanking: rankingScore가 최신 데이터 기준 값이 0 일 경우 createAt 데이터를 최신으로 업데이트
* 0이 아닐 경우 새로운 ranking 테이블을 생성함
* */
@Mapper
public interface RankingRepository {
    int save(RankingVO vo);
    int updateRankingScore(RankingVO vo); //@Param rankingType, userId, rankingScore

    int findRankingScore(RankingVO vo);
    int saveOrUpdateRanking(RankingVO vo);
    List<RankingVO> findAllByRankingType(@Param("rankingType") String rankingType);
    LinkedHashSet<RankingVO> findAllByPkSet(@Param("pkSet") LinkedHashSet<Object> pkSet);
    LinkedHashSet<RankingVO> findAllByPkSetAndFilterByTimeUnit(@Param("pkSet") LinkedHashSet<Object> pkSet, @Param("timeUnit") String timeUnit);
    void updateCreateAtToLatestDate();

}
