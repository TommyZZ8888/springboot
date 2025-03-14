package com.www.task.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;


//import static org.quartz.JobBuilder.*;
//import static org.quartz.TriggerBuilder.*;
//import static org.quartz.SimpleScheduleBuilder.*;
public class _06_QuartzTest_Data {

    public static void main(String[] args) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();


            JobDetail job = JobBuilder.newJob(HelloDataJob.class)
                    .usingJobData("detail-key-1","detail-value-1")
                    .usingJobData("hehe","a")
                    .withIdentity("job1", "group1")
                    .build();


            // 0 1 2 3
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .usingJobData("trigger-key-1","trigger-value-1")
                    .usingJobData("hehe","b")
                    .startNow()
                    .withSchedule(
                        CronScheduleBuilder.cronSchedule("* * * * * ? *")
                    )
                    .build();


            scheduler.scheduleJob(job, trigger);

            TimeUnit.SECONDS.sleep(1);
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
