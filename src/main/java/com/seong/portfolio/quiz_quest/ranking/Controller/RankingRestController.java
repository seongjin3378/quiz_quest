package com.seong.portfolio.quiz_quest.ranking.Controller;

import com.seong.portfolio.quiz_quest.ranking.service.RankingService;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import com.seong.portfolio.quiz_quest.ranking.vo.UserUsageTimerVO;
import com.seong.portfolio.quiz_quest.user.service.SessionService;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RankingRestController {
    private final RankingService rankingService;
    private static final Logger logger = LoggerFactory.getLogger(RankingRestController.class);
    private final SessionService sessionService;
    //private final ApplicationContext applicationContext;

    /* 각각의 버킷들이 생성 후 1분이 지나면 개별적으로 사라짐  */
    /*
    private final ExpiringMap<String, Bucket> buckets = ExpiringMap.builder()
            .expiration(1, TimeUnit.MINUTES).build();
    */
    /* 만료되면 람다식을 통해 userId : bucket 처럼 key/value 형태로 저장 됨*/


    public RankingRestController(RankingService rankingService, SessionService sessionService/*, ApplicationContext applicationContext*/) {
        this.rankingService = rankingService;
        //this.applicationContext = applicationContext;
        this.sessionService = sessionService;
    }

    @PostMapping("/user-usage-time")
    public ResponseEntity<Void> SaveUserUsageTime()
    {
        //rankingService.updateUserUsageTime();

        return ResponseEntity.ok().build();
    }
    @PostMapping("/saveUsageTimer")
    public ResponseEntity<Object> saveTimer(@RequestBody UserUsageTimerVO vo) {
        /*Bucket bucket = applicationContext.getBean("OneReqOneMinBucket", Bucket.class);
        if(bucket.tryConsume(1))
        {
            logger.info("요청 처리됨");
        }else{
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS) // 429 Too Many Requests
                    .body("요청 수를 초과했습니다. 잠시 후 다시 시도하세요.");
        }

         */
        String userId = sessionService.getSessionId();
        int result = 0 ;
        if(!userId.equals("anonymousUser")) {
            result = rankingService.updateUserUsageTime(vo.getUserUsageTimer());
        }
        logger.info("Saving timer: {}", vo.getUserUsageTimer());

        if(result == 1)  //랭킹 정보가 업데이트가 성공했을경우
        {
            return ResponseEntity.ok(1);
        }
        else{return ResponseEntity.ok(Integer.toString(result));}

    }
    private boolean hasConsumed(Bucket bucket)
    {
        return bucket.tryConsume(1);
    }
}
