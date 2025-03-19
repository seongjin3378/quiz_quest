package com.seong.portfolio.quiz_quest.quartz.rankings.job;

import com.seong.portfolio.quiz_quest.quartz.interfaces.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public class UsageTimeUpdateJob implements Job {
    @Qualifier("usageTimeUpdateJob")
    private final JobService usageTimeUpdateJobService;

    public UsageTimeUpdateJob(@Qualifier("UsageTimeUpdateJob") JobService usageTimeUpdateJobService) {

        this.usageTimeUpdateJobService = usageTimeUpdateJobService;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("UsageTimeUpdate 잡 실행");
        usageTimeUpdateJobService.execute();

        }

    }
