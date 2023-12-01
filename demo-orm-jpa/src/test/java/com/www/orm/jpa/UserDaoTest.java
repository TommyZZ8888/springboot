package com.www.orm.jpa;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.www.orm.DemoOrmJpaApplicationTests;
import com.www.orm.jpa.entity.User;
import com.www.orm.jpa.repository.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @Description UserDaoTest
 * @Author 张卫刚
 * @Date Created on 2023/12/1
 */
@Slf4j
public class UserDaoTest extends DemoOrmJpaApplicationTests {

    @Autowired
    private UserDao userDao;


    @Test
    public void testSave() {
        String salt = IdUtil.fastSimpleUUID();
        User testSave3 = User.builder().name("testSave3")
                .password(SecureUtil.md5("123456" + salt))
                .salt(salt).email("testSave3@xkcoding.com")
                .phoneNumber("17300000003")
                .status(1)
                .lastLoginTime(new DateTime())
                .build();
        userDao.save(testSave3);

        Assertions.assertNotNull(testSave3.getId());
        Optional<User> byId = userDao.findById(testSave3.getId());
        Assertions.assertTrue(byId.isPresent());
        log.debug("byId = {}", byId.get());

    }


}
