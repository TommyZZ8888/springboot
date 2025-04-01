
package com.www.task.examples.example1;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class HelloJob implements Job {

	public void execute(JobExecutionContext context) {
		System.out.println("Hello World! - " + new Date());
	}
}
