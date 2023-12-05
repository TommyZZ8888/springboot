package com.www.orm.mybatis.mapper.page.mapper;

import com.www.orm.mybatis.mapper.page.entity.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description UserMapper
 * @Author 张卫刚
 * @Date Created on 2023/12/5
 */
@Component
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
}
