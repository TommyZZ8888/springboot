package com.www.task.quartz.test;

import com.www.task.quartz.common.DFUtil;
import org.quartz.*;

import java.util.Date;
import java.util.StringJoiner;

@PersistJobDataAfterExecution
public class BadCatchJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            StringJoiner outStr = new StringJoiner(" | ")
                    .add("BadJob.execute")
                    .add(DFUtil.format(new Date()));
            System.out.println(outStr);

            context.getJobDetail().getJobDataMap().put("a",1);
            if (!context.getJobDetail().getJobDataMap().containsKey("xxx")) {
                throw new RuntimeException("xxx");
            }
            System.out.println("执行成功");
        } catch (Exception e) {
            System.out.println("--- Error in job!" + e.getMessage());
            context.getJobDetail().getJobDataMap().put("xxx", "00");

            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setUnscheduleAllTriggers(true);
            throw jobExecutionException;

//            try {
//                context.getScheduler().unscheduleJobs(
//                        context.getScheduler().getTriggersOfJob(context.getJobDetail().getKey())
//                                .stream().map(Trigger::getKey).collect(Collectors.toList())
//                );
//            } catch (SchedulerException schedulerException) {
//                schedulerException.printStackTrace();
//            }


            /**
             * 注意：
             * 1、除非知道如何修复，不然不要重新触发
             * 2、如果数据修改过，那么从哪里修改的，就从哪里取，不要从 getMergedJobDataMap 中获取
             */
//            JobExecutionException jobExecutionException = new JobExecutionException(e);
//            jobExecutionException.setRefireImmediately(true);
//            throw jobExecutionException;

//            try {
//                context.getScheduler().scheduleJob(
//                        TriggerBuilder.newTrigger().startNow().usingJobData("xxx", "00").forJob(context.getJobDetail()).build()
//                );
//            } catch (SchedulerException schedulerException) {
//                schedulerException.printStackTrace();
//            }

        }

    }
}


