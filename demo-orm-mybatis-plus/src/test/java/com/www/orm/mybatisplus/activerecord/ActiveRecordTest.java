package com.www.orm.mybatisplus.activerecord;

import com.www.orm.mybatisplus.DemoOrmMybatisPlusApplicationTests;
import com.www.orm.mybatisplus.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Description ActiveRecordTest
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
public class ActiveRecordTest extends DemoOrmMybatisPlusApplicationTests {

    @Test
    public void testActiveRecordInsert(){
        Role role = new Role();
        role.setName("VIP");
        Assertions.assertTrue(role.insert());
    }
}
