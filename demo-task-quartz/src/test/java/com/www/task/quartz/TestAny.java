package com.www.task.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class TestAny {
    @Test
    public void testCron() throws ParseException {
        /**
         * 通过，不一定正确
         * 不通过，一定不正确
         */


//        CronExpression.validateExpression("0 0 0 L-3 * ? 2020");
//        CronExpression.validateExpression("0 0 0 1-3 * ? 2020");
        CronExpression.validateExpression("0 0 10am 1,15 * ?");
    }


    @Test
    public void test() {
        LocalDateTime dateTime = LocalDateTime.now().plus(30, ChronoUnit.DAYS);
        System.out.println(StringUtils.capitalize("aaa"));
        System.out.println(StringUtils.uncapitalize("Hello"));
    }


    @Test
    public void test2() throws SchedulerException {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();


        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", "two");
        props.put("org.quartz.threadPool.threadCount", "2");
        props.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        Scheduler scheduler2 = new StdSchedulerFactory(props).getScheduler();

        System.out.println(scheduler);
        System.out.println(scheduler2);
        System.out.println(scheduler == scheduler2);
    }


}
