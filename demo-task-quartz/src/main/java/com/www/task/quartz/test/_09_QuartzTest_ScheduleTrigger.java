package com.www.task.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;


//import static org.quartz.JobBuilder.*;
//import static org.quartz.TriggerBuilder.*;
//import static org.quartz.SimpleScheduleBuilder.*;
public class _09_QuartzTest_ScheduleTrigger {

    public static void main(String[] args) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();


            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("job", "group1")
                    .storeDurably()
                    .build();

            // 0 3
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "group1")
                    .forJob("job", "group1")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(3)
                                    .repeatForever()
                    )
                    .build();

            scheduler.addJob(job,false);
            scheduler.scheduleJob(trigger);

            TimeUnit.SECONDS.sleep(3);
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
