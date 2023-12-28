package com.www.multiple.datasource.mybatisplus.service;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.www.multiple.datasource.mybatisplus.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


/**
 * @Description UserService
 * @Author 张卫刚
 * @Date Created on 2023/12/27
 */
// 默认master   application.yml配置
@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public void insert() {
        testMapper.insert();
    }

    /**
     * 最近原则
     */
    @DS("slave")
    public void insert2() {
        testMapper.insert();
    }

}
