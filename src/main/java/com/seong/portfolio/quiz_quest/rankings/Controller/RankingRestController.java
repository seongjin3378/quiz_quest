package com.seong.portfolio.quiz_quest.rankings.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seong.portfolio.quiz_quest.rankings.service.redis.RedisRankingService;
import com.seong.portfolio.quiz_quest.rankings.vo.UserUsageTimerVO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rankings")
public class RankingRestController {
    private static final Logger logger = LoggerFactory.getLogger(RankingRestController.class);
    private final SessionService sessionService;
    private final RedisRankingService redisRankingService;

    public RankingRestController(SessionService sessionService, RedisRankingService redisRankingService) {
        this.sessionService = sessionService;
        this.redisRankingService = redisRankingService;
    }

    @PutMapping("/usage-timers")
    public ResponseEntity<Object> saveTimer(@RequestBody UserUsageTimerVO vo) throws JsonProcessingException {
        String userId = sessionService.getSessionId();
        int result = 0 ;
        logger.info(userId);
        if(!userId.equals("anonymousUser")) {
           /* result = rankingService.updateUserUsageTime(vo.getUserUsageTimer());*/
            result = redisRankingService.updateUserUsageTime(vo.getUserUsageTimer(), "usage_time");
        }
        logger.info("Saving timer: {}", vo.getUserUsageTimer());

        if(result == 1)  //랭킹 정보가 업데이트가 성공했을경우
        {
            return ResponseEntity.ok(1);
        }
        else{return ResponseEntity.ok(Integer.toString(result));}

    }
}
