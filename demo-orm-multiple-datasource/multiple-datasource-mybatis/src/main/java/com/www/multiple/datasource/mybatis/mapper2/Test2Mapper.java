package com.www.multiple.datasource.mybatis.mapper2;

import com.www.multiple.datasource.mybatis.model.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description User1Mapper
 * @Author 张卫刚
 * @Date Created on 2023/12/27
 */
@Mapper
public interface Test2Mapper {

    int insert(Test test);

    List<Test> list();
}
