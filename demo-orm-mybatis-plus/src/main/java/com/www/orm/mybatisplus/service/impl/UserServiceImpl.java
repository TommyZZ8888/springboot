package com.www.orm.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.orm.mybatisplus.entity.User;
import com.www.orm.mybatisplus.mapper.UserMapper;
import com.www.orm.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description UserServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
