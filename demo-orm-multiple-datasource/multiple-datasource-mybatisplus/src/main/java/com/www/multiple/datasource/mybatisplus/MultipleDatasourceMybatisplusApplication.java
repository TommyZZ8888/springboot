package com.www.multiple.datasource.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.www.multiple.datasource.mybatisplus.mapper")
public class MultipleDatasourceMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultipleDatasourceMybatisplusApplication.class, args);
    }

}
