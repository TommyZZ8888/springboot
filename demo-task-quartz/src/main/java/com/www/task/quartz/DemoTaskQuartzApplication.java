package com.www.task.quartz;

import com.www.task.quartz.test.HelloJob;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@MapperScan("com.www.task.quartz.mapper") // 指定 Mapper 包路径
@SpringBootApplication
public class DemoTaskQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTaskQuartzApplication.class, args);
    }

//    @Bean
    CommandLineRunner commandLineRunner(Scheduler scheduler) {
        return args -> {
            Date runTime = evenMinuteDate(new Date());
            JobDetail job = newJob(HelloJob.class).withIdentity("job11", "group1").build();

            Trigger trigger = newTrigger().withIdentity("trigger11", "group1").startNow().build();

            scheduler.scheduleJob(job, trigger);
        };
    }
}
