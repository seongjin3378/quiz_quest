package com.seong.portfolio.quiz_quest.quartz;

import com.seong.portfolio.quiz_quest.quartz.user.job.UserUsageTimeJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail UserUsageTimeJobDetail() {
        return JobBuilder.newJob(UserUsageTimeJob.class)
                .withIdentity("UserUsageTimeJob")
                .storeDurably() // Job 이 트리거에 의해 실행 되지 않아도 JobStore에 영구 저장됨
                .build();
    }

    @Bean
    public Trigger UserUsageTimeTrigger()
    {
        //?는 특정날을 지정안함
        //*는 모든 달을 의미
        //왼쪽부터 초, 분, 시, 일, 월, 요일
        String cronExpression = "0 0 0 ? * MON";
        return TriggerBuilder.newTrigger()
                .forJob(UserUsageTimeJobDetail())
                .withIdentity("UserUsageTimeJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();
    }
}
