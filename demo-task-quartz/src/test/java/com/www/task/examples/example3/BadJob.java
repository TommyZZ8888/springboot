package com.www.task.examples.example3;

import com.www.task.quartz.common.DFUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.StringJoiner;

public class BadJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StringJoiner outStr = new StringJoiner(" | ")
                .add("BadJob.execute")
                .add(DFUtil.format(new Date()));
        System.out.println(outStr);

        throw new RuntimeException("xxx");
    }
}


