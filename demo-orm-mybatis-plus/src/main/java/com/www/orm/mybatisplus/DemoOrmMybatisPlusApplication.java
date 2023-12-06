package com.www.orm.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages={"com.www.orm.mybatisplus.mapper"})
@EnableTransactionManagement
public class DemoOrmMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOrmMybatisPlusApplication.class, args);
    }

}
