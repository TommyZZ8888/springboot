package com.www.rbac.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description JwtConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtConfig {

    /**
     * jwt 加密key，默认 tommy
     */
    private String key = "tommy";

    /**
     * jwt 过期时间 默认值600000 十分钟
     */
    private Long ttl = 600000L;

    /**
     *  刷新令牌的过期时间  开启记住我之后 jwt过期时间 默认值60480000 7天
     */
    private Long remember = 604800000L;
}
