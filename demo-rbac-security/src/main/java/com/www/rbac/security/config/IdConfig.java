package com.www.rbac.security.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description IdConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@Configuration
public class IdConfig {

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(1, 1);
    }

}
