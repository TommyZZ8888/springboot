package com.www.task.quartz.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;


//import static org.quartz.JobBuilder.*;
//import static org.quartz.TriggerBuilder.*;
//import static org.quartz.SimpleScheduleBuilder.*;
public class _04_QuartzTest_NoNameAndGroup {

    public static void main(String[] args) {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();


            JobDetail job = JobBuilder.newJob(HelloJob.class)
//                    .withIdentity("job1", "group1")
                    .build();




            // 0 1 2 3
            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(1)
                                    .withRepeatCount(1)
//                                    .repeatForever()
                    )
                    .build();


            scheduler.scheduleJob(job, trigger);

            TimeUnit.SECONDS.sleep(3);
            scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
