
package com.www.task.examples.example1;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

public class SimpleJob implements Job {

    public void execute(JobExecutionContext context) {

        JobKey jobKey = context.getJobDetail().getKey();
        System.out.println("SimpleJob says: " + jobKey + " executing at " + new Date());
    }

}
