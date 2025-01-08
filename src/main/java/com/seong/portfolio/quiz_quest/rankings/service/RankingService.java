package com.seong.portfolio.quiz_quest.rankings.service;

import com.seong.portfolio.quiz_quest.rankings.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;


/* updateUserUsageTime
    클라이언트가 60초 단위로 POST를 보낸 rankingScore 값을 db에 업데이트 하는  역할을 함
    업데이트가 성공 시 return 1을 반환
    rankingScore가 자바스크립트로 조작됬을 시 return "이전사용시간 값"을 브라우저에 반환하는 역할을 함

*/

public interface RankingService {
    int updateUserUsageTime(int rankingScore);

    int findRankingScore(RankingVO vo);
    int saveOrUpdateRanking(RankingVO vo);
    void initializeRankingDB(UserVO vo);
}
