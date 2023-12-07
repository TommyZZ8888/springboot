package com.www.upload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description IndexController
 * @Author 张卫刚
 * @Date Created on 2023/12/7
 */
@Controller
public class IndexController {

    @GetMapping("")
    public String index() {
        return "index";
    }
}
