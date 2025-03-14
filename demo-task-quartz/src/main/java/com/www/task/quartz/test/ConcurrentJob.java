package com.www.task.quartz.test;

import com.www.task.quartz.common.DFUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

@DisallowConcurrentExecution
public class ConcurrentJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(
                new StringJoiner(" | ")
                        .add(DFUtil.format(new Date()))
                        .add(String.valueOf(this.hashCode()))
        );
    }
}
