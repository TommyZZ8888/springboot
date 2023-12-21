package com.www.config.consul.controller;

import com.www.config.consul.config.DbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description DbController
 * @Author 张卫刚
 * @Date Created on 2023/12/21
 */
@CrossOrigin
@RestController
@RequestMapping("/consul")
@RefreshScope // 如果参数变化，自动刷新
public class DbController {

    @Autowired
    private DbConfig config ;

    @Value("${author.name}")
    private String name;

    @GetMapping("/config/get")
    public ResponseEntity getConfig(){
        return ResponseEntity.ok(config);
    }

    @GetMapping("/config/getName")
    public String getName(){
        return name;
    }


}
