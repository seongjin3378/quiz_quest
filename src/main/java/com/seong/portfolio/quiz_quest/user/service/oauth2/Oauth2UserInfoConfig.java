package com.seong.portfolio.quiz_quest.user.service.oauth2;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class Oauth2UserInfoConfig {

/*    @Bean
    @Qualifier("google")
    public GoogleUserInfo google(Map<String, Object> attributes)
    {
        return new GoogleUserInfo(attributes);
    }

    @Bean
    @Qualifier("github")
    public GithubUserInfo github(Map<String, Object> attributes)
    {
        return new GithubUserInfo(attributes);
    }*/
}
