package com.www.multiple.datasource.mybatis.service;

import com.www.multiple.datasource.mybatis.mapper.Test1Mapper;
import com.www.multiple.datasource.mybatis.mapper2.Test2Mapper;
import com.www.multiple.datasource.mybatis.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description UserService
 * @Author 张卫刚
 * @Date Created on 2023/12/27
 */
@Service
public class TestService {

    @Autowired
    private Test1Mapper test1Mapper;

    @Autowired
    private Test2Mapper test2Mapper;

    public List<Test> userList1() {
        return test1Mapper.list();
    }

    public List<Test> userList2() {
        return test2Mapper.list();
    }

    public void insert1() {
        test1Mapper.insert(new Test());
    }

    public void insert2() {
        test2Mapper.insert(new Test());
    }
}
