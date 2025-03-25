package com.www.task.quartz.config;

import com.www.task.quartz.job.SpringJob1;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//@Component
public class JobInit {

    @Autowired
    public Scheduler scheduler;

    @PostConstruct
    public void initjob() throws SchedulerException {
        System.out.println("=-=-=-"+scheduler.getMetaData().getThreadPoolSize());

        JobDetail detail = JobBuilder.newJob(SpringJob1.class)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .build();

        scheduler.scheduleJob(detail, trigger);
    }

}
