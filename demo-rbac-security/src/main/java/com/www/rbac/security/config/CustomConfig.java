package com.www.rbac.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description CustomConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@ConfigurationProperties(prefix = "custom.config")
@Data
public class CustomConfig {

    /**
     * 不需要拦截的地址
     */
    private IgnoreConfig ignores;

}
