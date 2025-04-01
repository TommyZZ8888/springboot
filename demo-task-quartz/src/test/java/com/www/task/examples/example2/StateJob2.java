package com.www.task.examples.example2;

import com.www.task.quartz.common.DFUtil;
import org.quartz.*;

import java.util.Date;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StateJob2 implements Job {
    private static final AtomicInteger index = new AtomicInteger(0);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        int tempIndex = index.getAndAdd(1);
        if (tempIndex == 1) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        System.out.println(
                new StringJoiner(" | ")
                        .add(DFUtil.format(new Date()))
                        .add(String.valueOf(this.hashCode()))
                        .add(jobDataMap.getString("a"))
        );

        if (tempIndex == 0) {
            jobDataMap.put("a", "b");
        } else if (tempIndex == 1) {
            jobDataMap.put("a", "c");
        }

    }
}
