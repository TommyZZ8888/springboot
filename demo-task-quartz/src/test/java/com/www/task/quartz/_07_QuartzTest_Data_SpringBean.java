package com.www.task.quartz;

import com.www.task.quartz.test.HelloDataJob;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;


@SpringBootTest
public class _07_QuartzTest_Data_SpringBean {

    @Test
    public void test() {
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
