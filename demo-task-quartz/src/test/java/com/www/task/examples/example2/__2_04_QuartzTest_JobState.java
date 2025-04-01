package com.www.task.examples.example2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class __2_04_QuartzTest_JobState {
    private static SchedulerFactory sf = new StdSchedulerFactory();
    private static Scheduler sched;

    @BeforeAll
    public static void beforeClass() throws SchedulerException {
        sched = sf.getScheduler();
        sched.start();
    }

    /**
     * 2020-09-27 15:41:32 | 1682627988     100   50--->
     * 2020-09-27 15:41:33 | 1495355997           启动了
     * 2020-09-27 15:41:34 | 784793122
     */
    @Test
    public void concurrentExecution() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(ConcurrentJob.class).withIdentity("job1").build();

        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).withRepeatCount(2)).build();

        sched.scheduleJob(job1, trigger1);

        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void persistJobDataAfterExecution() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(StateJob.class).withIdentity("job1")
                .usingJobData("a","a").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).withRepeatCount(1)).build();
        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(3);
    }



    @Test
    public void persistJobDataAfterExecution2() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(StateJob2.class).withIdentity("job1")
                .usingJobData("a","a").build();

        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(simpleSchedule().withIntervalInSeconds(1).withRepeatCount(2)).build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(3);
    }



}
