package com.www.properties.controller;

import cn.hutool.core.lang.Dict;
import com.www.properties.domain.AppPropertiesEntity;
import com.www.properties.domain.DevPropertiesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description PropertiesController
 * @Author 张卫刚
 * @Date Created on 2023/11/20
 */
@RestController
public class PropertiesController {

    private final AppPropertiesEntity applicationProperty;
    private final DevPropertiesEntity developerProperty;

    @Autowired
    public PropertiesController(AppPropertiesEntity applicationProperty, DevPropertiesEntity developerProperty) {
        this.applicationProperty = applicationProperty;
        this.developerProperty = developerProperty;
    }

    @GetMapping("/property")
    public Dict index() {
        return Dict.create().set("applicationProperty", applicationProperty).set("developerProperty", developerProperty);
    }

    @GetMapping("/version")
    public String version() {
        return applicationProperty.getVersion();
    }

    @GetMapping("/name")
    public String name() {
        return developerProperty.getName();
    }
}
