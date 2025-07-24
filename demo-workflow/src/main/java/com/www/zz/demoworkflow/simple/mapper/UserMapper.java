package com.www.zz.demoworkflow.simple.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: UserMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:14
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
