package com.www.task.quartz.config;

import com.www.task.quartz.job.SpringJob1;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//@Component
public class JobClusterInit {

    @Autowired
    private Scheduler scheduler;

    @Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
    private String instanceName;

    @PostConstruct
    public void initjob() throws SchedulerException {

        System.out.println("=-=-=-" + instanceName);

        System.out.println("=-=-=-" + scheduler.getMetaData().getThreadPoolSize());

        startSpringJob("job-2", "trigger-2");
        startSpringJob("job-3", "trigger-3");
        startSpringJob("job-1", "trigger-1");
    }

    private void startSpringJob(String jobName, String triggerName) throws SchedulerException {

        JobDetail detail = JobBuilder.newJob(SpringJob1.class)
                .usingJobData("key0","b")
                .withIdentity(jobName)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .build();


        scheduler.scheduleJob(detail, trigger);
    }

}
