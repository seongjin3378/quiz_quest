package com.seong.portfolio.quiz_quest.settings.bucket4j;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import java.time.Duration;

@Configuration
public class RateLimitingConfig {

    @Bean
    @Scope("prototype")
    //@Qualifier("OneReqOneMinBucket") 기본적으로 @Bean 형태 주입은 Qualifer를 지원하지 않음
    public Bucket saveUsageTimerBucket()
    {
        Refill refill = Refill.greedy(1, Duration.ofMillis(59000)); //1분당 1개의 요청을 리필
        Bandwidth bandWidth = Bandwidth.classic(1, refill); // 1개의 요청만 허용하고, 리필을 설정함
        return Bucket.builder().addLimit(bandWidth).build();
    }
}
