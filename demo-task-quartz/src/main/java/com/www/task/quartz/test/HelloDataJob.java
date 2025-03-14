package com.www.task.quartz.test;


import com.www.task.quartz.common.DFUtil;
import com.www.task.quartz.common.SpringContextUtil;
import org.quartz.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.StringJoiner;

public class HelloDataJob implements Job {

    private String hehe;

    public void setHehe(String hehe) {
        this.hehe = hehe;
    }

    HelloSpringService helloSpringService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        helloSpringService = (HelloSpringService) SpringContextUtil.applicationContext
                .getBean(StringUtils.uncapitalize(HelloSpringService.class.getSimpleName()));


        JobDetail jobDetail = context.getJobDetail();

        Trigger trigger = context.getTrigger();

        StringJoiner outStr = new StringJoiner(" ")
                .add("HelloJob.execute")
                .add(DFUtil.format(new Date()))
                .add(Thread.currentThread().getName())
                .add(context.getTrigger().getKey().getName());
        System.out.println(outStr);

//        System.out.println(jobDetail.getJobDataMap().get("hehe"));
//        System.out.println(trigger.getJobDataMap().get("hehe"));

//        System.out.println(context.getMergedJobDataMap().get("hehe"));
//
//
//        System.out.println(helloSpringService);

        System.out.println("=-=-= "+hehe);
    }
}


