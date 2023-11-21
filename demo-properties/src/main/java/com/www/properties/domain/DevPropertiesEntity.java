package com.www.properties.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description DevPropertiesEntity
 * @Author 张卫刚
 * @Date Created on 2023/11/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "developer")
public class DevPropertiesEntity {
    private String name;
    private String website;
    private String qq;
    private String phoneNumber;
}
