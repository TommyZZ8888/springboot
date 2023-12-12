package com.www.cache.redis.service;

import com.www.cache.redis.DemoCacheRedisApplication;
import com.www.cache.redis.DemoCacheRedisApplicationTests;
import com.www.cache.redis.entity.User;
import com.www.cache.redis.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description UserServiceTest
 * @Author 张卫刚
 * @Date Created on 2023/12/12
 */
@Slf4j
public class UserServiceTest extends DemoCacheRedisApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void getAfterSave() {
        userService.saveOrUpdate(new User(4L, "测试中文"));

        User user = userService.get(4L);
        log.debug("【user】= {}", user);
        // 查看日志，只打印保存用户的日志，查询是未触发查询日志，因此缓存生效
        System.out.printf("");
    }
}
