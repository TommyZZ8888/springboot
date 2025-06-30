package com.www.task.quartz.config;

import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @Describtion: JobConfig 微服务下单独的quartz库 配置数据源
 * @Author: 张卫刚
 * @Date: 2025/6/30 16:42
 */
@Configuration
public class JobConfig {

	@Bean
	@QuartzDataSource
	public DataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306/quartz", "root", "root");
		return driverManagerDataSource;
	}

}
