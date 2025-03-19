package com.seong.portfolio.quiz_quest.quartz.rankings.job;

import com.seong.portfolio.quiz_quest.quartz.interfaces.JobService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.transaction.annotation.Transactional;


public class InitializeUsageTimeJob implements Job {
    @Qualifier("InitializeUsageTimeJob")
    private final JobService rankingJobService;

    public InitializeUsageTimeJob(@Qualifier("InitializeUsageTimeJob") JobService rankingJobService) {
        this.rankingJobService = rankingJobService;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        rankingJobService.execute();


    }
}
