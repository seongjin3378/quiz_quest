package com.seong.portfolio.quiz_quest.quartz;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@RequiredArgsConstructor
public class QuartzSchedulerConfig {
    private final Scheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(QuartzSchedulerConfig.class);
    private final ApplicationContext applicationContext;
    private final JobDetail UserUsageTimeJobDetail;

    @PostConstruct
    public void startScheduler()
    {
        try{
            if(!scheduler.isStarted())
            {
                logger.info("스케줄러 시작");
                scheduler.start();

                /* 즉시 실행 테스트
                JobDetail userUsageTimeJobDetail = (JobDetail) applicationContext.getBean("UserUsageTimeJobDetail");
                scheduler.triggerJob(userUsageTimeJobDetail.getKey());
                logger.info("트리거 즉시 실행");

                 */

            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
