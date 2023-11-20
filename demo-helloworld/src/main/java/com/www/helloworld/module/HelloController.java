package com.www.helloworld.module;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description HelloController
 * @Author 张卫刚
 * @Date Created on 2023/11/20
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@RequestParam(required = false, name = "name") String name) {
        String who = StringUtils.hasLength(name) ? name : "who";
        return StrUtil.format("hello {}!", who);
    }
}
