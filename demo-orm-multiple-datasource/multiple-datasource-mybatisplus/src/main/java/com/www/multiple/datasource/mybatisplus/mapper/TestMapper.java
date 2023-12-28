package com.www.multiple.datasource.mybatisplus.mapper;

import com.www.multiple.datasource.mybatisplus.model.Test;

import java.util.List;

/**
 * @Description TestMapper
 * @Author 张卫刚
 * @Date Created on 2023/12/27
 */
//@DS("master")
public interface TestMapper{
    int insert();

    List<Test> list();
}
