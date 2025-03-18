package com.www.task.quartz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.www.task.quartz.mapper") // 指定 Mapper 包路径
@SpringBootApplication
public class DemoTaskQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoTaskQuartzApplication.class, args);
    }

}
