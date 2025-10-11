package com.www.orm.mybatisplus;

import com.www.orm.mybatisplus.entity.ViolationEntity;
import com.www.orm.mybatisplus.service.ViolationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DemoOrmMybatisPlusApplicationTests {

    @Autowired
    private ViolationService violationService;
    @Test
    void contextLoads() {
        List<ViolationEntity> list = violationService.list();
        System.out.println();
    }

}
