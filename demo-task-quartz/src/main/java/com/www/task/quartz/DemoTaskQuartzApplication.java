package com.www.task.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.www.task.quartz.mapper")
@SpringBootApplication
public class DemoTaskQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTaskQuartzApplication.class, args);
    }

}
