package com.www.task.quartz;

import com.www.task.quartz.test.BadCatchJob;
import com.www.task.quartz.test.BadJob;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class __2_06_QuartzTest_BadJob {
    private static Scheduler sched;

    @BeforeAll
    public static void beforeClass() throws SchedulerException {
        sched = new StdSchedulerFactory().getScheduler();
        sched.start();
    }


    @Test
    public void normal() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(BadJob.class).withIdentity("job1")
                .build();

        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInSeconds(1)
                                .withRepeatCount(3)
                ).build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(5);
    }


    @Test
    public void persistJobDataAfterExecution2() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(BadCatchJob.class).withIdentity("job1")
                .build();

        Trigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInSeconds(1)
                                .withRepeatCount(3)
                ).build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(8);
    }



}
