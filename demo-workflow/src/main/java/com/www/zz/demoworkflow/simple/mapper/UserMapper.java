package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.UserEntity;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserEntity record);

    int insertSelective(UserEntity record);

    UserEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserEntity record);

    int updateByPrimaryKey(UserEntity record);
}