package com.www.file.download.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Describtion: IndexController
 * @Author: 张卫刚
 * @Date: 2024/1/1 12:39
 */
@Controller
public class IndexController {
    @GetMapping("")
    public String index() {
        return "index";
    }
}
