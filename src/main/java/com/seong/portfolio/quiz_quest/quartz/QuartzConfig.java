package com.seong.portfolio.quiz_quest.quartz;

import com.seong.portfolio.quiz_quest.quartz.rankings.job.InitializeUsageTimeJob;
import com.seong.portfolio.quiz_quest.quartz.rankings.job.UsageTimeUpdateJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail UsageTimeUpdateJobDetail() {
        return JobBuilder.newJob(UsageTimeUpdateJob.class)
                .withIdentity("UsageTimeUpdateJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger UsageTimeUpdateTrigger() {
        String cronExpression = "0 */1 * * * ?"; //5분마다 실행

        return TriggerBuilder.newTrigger()
                .forJob(UsageTimeUpdateJobDetail())
                .withIdentity("UsageTimeUpdateJob")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail InitializeUsageTimeJobDetail() {
        return JobBuilder.newJob(InitializeUsageTimeJob.class)
                .withIdentity("InitializeUsageTimeJob")
                .storeDurably() // Job 이 트리거에 의해 실행 되지 않아도 JobStore에 영구 저장됨
                .build();
    }

    @Bean
    public Trigger InitializeUsageTimeJobTrigger()
    {
        //?는 특정날을 지정안함
        //*는 모든 달을 의미
        //왼쪽부터 초, 분, 시, 일, 월, 요일
        String cronExpression = "0 0 0 ? * MON";

        return TriggerBuilder.newTrigger()
                .forJob(InitializeUsageTimeJobDetail())
                .withIdentity("InitializeUsageTimeJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();
    }
}
