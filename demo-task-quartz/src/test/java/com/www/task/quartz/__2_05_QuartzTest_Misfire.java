package com.www.task.quartz;

import com.www.task.quartz.common.DFUtil;
import com.www.task.quartz.test.HelloJob;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class __2_05_QuartzTest_Misfire {
    private static Scheduler sched;

    @BeforeAll
    public static void beforeClass() throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        sched = sf.getScheduler();
        sched.start();
    }


    @Test
    public void normal() throws SchedulerException, InterruptedException {
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startNow()
                .withSchedule(
                        simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void future() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-5, DateBuilder.IntervalUnit.SECOND);
        System.out.println(DFUtil.format(startTime));
        System.out.println(DFUtil.format(new Date()));

//        SimpleScheduleBuilder.simpleSchedule().mis
//        CronScheduleBuilder.cronSchedule("").mis
//        CalendarIntervalScheduleBuilder.calendarIntervalSchedule().mis
//        DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().mis
    }


    /**
     * 应当触发的时间2020-10-01 23:30:28
     * HelloJob.execute 2020-10-01 23:30:31 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:30:33 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:30:35 CustomizeQuartzScheduler_Worker-3 trigger1
     * HelloJob.execute 2020-10-01 23:30:37 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:30:39 CustomizeQuartzScheduler_Worker-2 trigger1
     *
     * 会把项目启动时间作为 任务开始时间，然后往后正常执行
     */

    @Test
    public void simple_default() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 应当触发的时间2020-10-01 23:32:04
     * HelloJob.execute 2020-10-01 23:32:07 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:32:09 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:32:11 CustomizeQuartzScheduler_Worker-3 trigger1
     * HelloJob.execute 2020-10-01 23:32:13 CustomizeQuartzScheduler_Worker-1 trigger1
     *
     * 会把项目启动时间作为 任务开始时间立马补一次，然后把剩余的次数执行完
     */
    @Test
    public void simple_FireNow() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionFireNow()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     *应当触发的时间2020-10-01 23:41:19
     * HelloJob.execute 2020-10-01 23:41:22 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:41:24 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:41:26 CustomizeQuartzScheduler_Worker-3 trigger1
     * HelloJob.execute 2020-10-01 23:41:28 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:41:30 CustomizeQuartzScheduler_Worker-2 trigger1
     *
     * 会把项目启动时间作为 任务开始时间，然后往后正常执行
     */
    @Test
    public void simple_NowWithExistingCount() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionNowWithExistingCount()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 应当触发的时间2020-10-01 23:42:12
     * HelloJob.execute 2020-10-01 23:42:15 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:42:17 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:42:19 CustomizeQuartzScheduler_Worker-3 trigger1
     * HelloJob.execute 2020-10-01 23:42:21 CustomizeQuartzScheduler_Worker-1 trigger1
     *
     * 会在启动时补一次，然后把项目启动时间作为 任务开始时间，然后往后正常执行
     */

    @Test
    public void simple_NowWithRemainingCount() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionNowWithRemainingCount()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 应当触发的时间2020-10-01 23:36:46
     * HelloJob.execute 2020-10-01 23:36:49 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:36:49 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:36:50 CustomizeQuartzScheduler_Worker-3 trigger1
     * HelloJob.execute 2020-10-01 23:36:52 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:36:54 CustomizeQuartzScheduler_Worker-2 trigger1
     *
     * 立马补上失火的任务，然后按照原计划继续执行
     */
    @Test
    public void simple_IgnoreMisfires() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionIgnoreMisfires()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


    /**
     *应当触发的时间2020-10-01 23:38:21
     * HelloJob.execute 2020-10-01 23:38:25 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:38:27 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:38:29 CustomizeQuartzScheduler_Worker-3 trigger1
     *
     * 忽略失火的任务，然后按照原计划继续执行
     */
    @Test
    public void simple_NextWithExistingCount() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionNextWithExistingCount()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 应当触发的时间2020-10-01 23:39:20
     * HelloJob.execute 2020-10-01 23:39:24 CustomizeQuartzScheduler_Worker-1 trigger1
     * HelloJob.execute 2020-10-01 23:39:26 CustomizeQuartzScheduler_Worker-2 trigger1
     * HelloJob.execute 2020-10-01 23:39:28 CustomizeQuartzScheduler_Worker-3 trigger1
     *
     * 忽略失火的任务，然后按照原计划继续执行
     */
    @Test
    public void simple_NextWithRemainingCount() throws SchedulerException, InterruptedException {
        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
        System.out.println("应当触发的时间" + DFUtil.format(startTime));
        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
        SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(4)
                                .withIntervalInSeconds(2)
                                .withMisfireHandlingInstructionNextWithRemainingCount()
                )
                .build();

        sched.scheduleJob(job1, trigger1);
        TimeUnit.SECONDS.sleep(10);
    }


//    @Test
//    public void simple_NextWithRemainingCount0() throws SchedulerException, InterruptedException {
//        Date startTime = DateBuilder.futureDate(-3, DateBuilder.IntervalUnit.SECOND);
//        System.out.println("应当触发的时间" + DFUtil.format(startTime));
//        JobDetail job1 = newJob(HelloJob.class).withIdentity("job1").build();
//        CronTrigger trigger1 = newTrigger().withIdentity("trigger1").startAt(startTime)
//                .withSchedule(
//                        CronScheduleBuilder.cronSchedule("xxx")
//                )
//                .build();
//
//        sched.scheduleJob(job1, trigger1);
//        TimeUnit.SECONDS.sleep(10);
//    }


}
