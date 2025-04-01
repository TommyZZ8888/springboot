package com.www.task.examples.example1;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

public class SimpleExample {

	@Test
	void run() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		Date runTime = evenMinuteDate(new Date());
		JobDetail job = newJob(HelloJob.class).withIdentity("job11", "group1").build();

		Trigger trigger = newTrigger().withIdentity("trigger11", "group1").startAt(runTime).build();

		sched.scheduleJob(job, trigger);
		sched.start();

		try {
			Thread.sleep(30000L);
		} catch (Exception e) {
		}

		sched.shutdown(true);
	}

}
