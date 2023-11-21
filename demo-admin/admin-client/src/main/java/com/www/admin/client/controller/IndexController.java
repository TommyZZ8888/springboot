package com.www.admin.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description IndexController
 * @Author 张卫刚
 * @Date Created on 2023/11/21
 */
@RestController
public class IndexController {

    @GetMapping(value = {"", "/"})
    public String index() {
        return "This is a Spring Boot Admin Client.";
    }

}
