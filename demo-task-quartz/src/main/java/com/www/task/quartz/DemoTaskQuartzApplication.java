package com.www.task.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.www.task.quartz.mapper"})
@SpringBootApplication
public class DemoTaskQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTaskQuartzApplication.class, args);
    }

}
