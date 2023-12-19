package com.www.task.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description BaseJob
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
public interface BaseJob extends Job {
    @Override
     default void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
