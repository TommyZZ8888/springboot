package com.www.properties.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description AppPropertiesEntity
 * @Author 张卫刚
 * @Date Created on 2023/11/20
 */
@Data
@Component
public class AppPropertiesEntity {

    @Value("${application.name}")
    private String name;

    @Value("${application.version}")
    private String version;
}
