package com.www.multiple.datasource.mybatisplus;

import com.www.multiple.datasource.mybatisplus.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultipleDatasourceMybatisplusApplicationTests {

    @Autowired
    private TestService testService;

    @Test
    void contextLoads() {
        testService.insert();
        testService.insert2();
    }


}
