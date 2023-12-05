package com.www.orm.mybatis.mapper.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = {"com.www.orm.mybatis.mapper.page.mapper"})
@SpringBootApplication
public class DemoOrmMybatisMapperPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoOrmMybatisMapperPageApplication.class, args);
    }

}
