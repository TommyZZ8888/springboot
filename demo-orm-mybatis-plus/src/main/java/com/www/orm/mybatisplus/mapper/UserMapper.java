package com.www.orm.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.orm.mybatisplus.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Description UserMapper
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
