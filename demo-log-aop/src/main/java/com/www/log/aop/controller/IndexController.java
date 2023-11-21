package com.www.log.aop.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.www.log.aop.anno.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description IndexController
 * @Author 张卫刚
 * @Date Created on 2023/11/21
 */
@RestController
@Slf4j
public class IndexController {


    @GetMapping("test")
    @OperateLog
    public Dict test(String who) {
        return Dict.create().set("who", StrUtil.isBlank(who) ? "me" : who);
    }


    @GetMapping("/testJson")
    public Dict testJson(@RequestBody Map<String, Object> map) {
        String jsonStr = JSONUtil.toJsonStr(map);
        log.info(jsonStr);
        return Dict.create().set("json", jsonStr);
    }

}
