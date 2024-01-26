package com.www.rbac.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description WebMvcConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

public static final Long MAX_AGE_SECS = 3600L;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**")
               .allowedOrigins("*")
               .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
               .maxAge(MAX_AGE_SECS);
    }
}
