package com.seong.portfolio.quiz_quest.rankings.repo;


import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*
ranking Type
- usage_time : 사용자들의 게시판 이용 시간을 랭킹 순으로 보여줌 (주, 월 단위)
- total_problem : 총 문제를 맞춘 횟수를 랭킹 순으로 보여줌
- language : 언어 별로 문제를 많이
*/

/* findTopNByOrderByRankingScoreDesc*/
/*  RankingScore가 높은 순으로 내림차순으로 n개의 데이터를 가져옴 */
@Mapper
public interface RankingRepository {
    int save(RankingVO vo);
    int updateRankingScore(RankingVO vo);

    int findRankingScore(RankingVO vo);
    int saveOrUpdateRanking(RankingVO vo);
    List<RankingVO> findTopNByOrderByRankingScoreDesc(int n);
    int existsByUserId(RankingVO vo);
}
