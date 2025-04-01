package com.www.task.examples.example1;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.www.task.quartz.entity.domain.JobAndTrigger;
import org.junit.jupiter.api.Test;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;


import java.util.Date;
import java.util.Properties;


public class SimpleTriggerExample {

	@Test
	void run() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1").build();

		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();

		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {

		}

		sched.shutdown(true);

		SchedulerMetaData metaData = sched.getMetaData();
		System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

	}

	@Test
	void run2() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job2", "group1").build();

		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2", "group1").startAt(startTime).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();


		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		sched.shutdown(true);

	}

	@Test
	void run3() throws Exception {
		//配置自动启动start()
//    Properties props = new Properties();
//    props.put("org.quartz.scheduler.startupOnLoad", "true");
//    SchedulerFactory sf = new StdSchedulerFactory(props);

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
		JobDetail job = newJob(SimpleJob.class).withIdentity("job3", "group1").build();
		SimpleTrigger trigger = newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();
		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
		} catch (Exception e) {
		}

		sched.shutdown();
	}

	@Test
	void run4() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job4", "group1").build();

		SimpleTrigger trigger = newTrigger().withIdentity("trigger4", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();

		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		sched.shutdown(true);

	}

	@Test
	void run5() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		JobDetail job = newJob(SimpleJob.class).withIdentity("job5", "group1").build();

		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger5", "group1")
				.startAt(futureDate(5, IntervalUnit.MINUTE)).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();

		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		sched.shutdown(true);
	}

	@Test
	void run6() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job6", "group1").build();

		SimpleTrigger trigger = newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		sched.start();


		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		sched.shutdown(true);
	}

	@Test
	void run7() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job7", "group1").build();

		SimpleTrigger trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(1).withRepeatCount(20)).build();

		Date ft = sched.scheduleJob(job, trigger);
		System.out.println(job.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");
		sched.start();
		try {
			// wait 33 seconds to show jobs
			Thread.sleep(300L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		sched.shutdown(true);
	}

	@Test
	void run8() throws Exception {

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		JobDetail job = newJob(SimpleJob.class).withIdentity("job8", "group1").storeDurably().build();

		sched.addJob(job, true);

		sched.triggerJob(jobKey("job8", "group1"));

		SimpleTrigger trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(1).withRepeatCount(20)).build();

		Date ft = sched.rescheduleJob(trigger.getKey(), trigger);
		sched.start();
		try {
			// wait five minutes to show jobs
			Thread.sleep(300L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}
		sched.shutdown(true);

	}
}
