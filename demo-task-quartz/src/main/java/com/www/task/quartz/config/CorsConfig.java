package com.www.task.quartz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Describtion: WebConfigMvc
 * @Author: 张卫刚
 * @Date: 2025/3/18 15:02
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 允许所有路径
				.allowedOrigins("http://localhost:63342") // 允许的前端地址
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
				.allowedHeaders("*") // 允许的请求头
				.allowCredentials(true) // 允许携带凭证（如 cookies）
				.maxAge(3600); // 预检请求的缓存时间
	}
}