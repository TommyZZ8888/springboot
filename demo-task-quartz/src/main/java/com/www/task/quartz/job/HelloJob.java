package com.www.task.quartz.job;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description HelloJob
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
@Slf4j
public class HelloJob implements BaseJob{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.error("HelloJob 执行时间: {}", DateUtil.now());
    }
}
