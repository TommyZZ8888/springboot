package com.www.zz.demoredisom;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRedisDocumentRepositories(basePackages = "com.www.zz.demoredisom.dao")
@EnableRedisEnhancedRepositories(basePackages = "com.www.zz.demoredisom.hash")
public class DemoRedisOmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRedisOmApplication.class, args);
	}

}
