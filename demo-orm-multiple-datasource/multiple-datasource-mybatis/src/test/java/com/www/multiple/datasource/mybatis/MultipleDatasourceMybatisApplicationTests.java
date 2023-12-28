package com.www.multiple.datasource.mybatis;

import com.www.multiple.datasource.mybatis.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MultipleDatasourceMybatisApplicationTests {

    @Autowired
    private TestService testService;

    @Test
    void contextLoads() {
        testService.insert1();
        testService.insert2();

        testService.userList1().forEach(item->{
            System.out.println(item.getName());
        });

        testService.userList2().forEach(item->{
            System.out.println(item.getName());
        });
    }

}
