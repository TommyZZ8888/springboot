package com.www.task.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class _01_QuartzTest {

    public static void main(String[] args) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();


            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("job1", "group1")
                    .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(job, trigger);
            // 0 5 10 15 20
            TimeUnit.SECONDS.sleep(20);
            scheduler.shutdown();

        } catch (SchedulerException | InterruptedException se) {
            se.printStackTrace();
        }
    }
}
