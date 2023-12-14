package com.www.cache.ehcache;

import com.www.cache.ehcache.entity.User;
import com.www.cache.ehcache.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoCacheEhcacheApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User(2L, "2");
        userService.saveOrUpdate(user);
    }

}
