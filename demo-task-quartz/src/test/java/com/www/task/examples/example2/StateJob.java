package com.www.task.examples.example2;

import com.www.task.quartz.common.DFUtil;
import org.quartz.*;

import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@PersistJobDataAfterExecution
public class StateJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println(
                new StringJoiner(" | ")
                        .add(DFUtil.format(new Date()))
                        .add(String.valueOf(this.hashCode()))
                        .add(jobDataMap.getString("a"))
        );

        jobDataMap.put("a","b");
    }
}
