package com.www.task.config;

import com.www.task.utils.MyThreadFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Description TaskConfig
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
@ComponentScan(basePackages = "com.www.task.job")
@EnableScheduling
@Configuration
public class TaskConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(20, MyThreadFactory.create("Job-Thread"));
    }
}
