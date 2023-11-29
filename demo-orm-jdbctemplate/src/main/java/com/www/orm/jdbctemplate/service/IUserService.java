package com.www.orm.jdbctemplate.service;

import com.www.orm.jdbctemplate.entity.User;

import java.util.List;

/**
 * @Description IUserService
 * @Author 张卫刚
 * @Date Created on 2023/11/29
 */
public interface IUserService {

    Boolean save(User user);

    Boolean delete(Long id);

    Boolean update(User user,Long id);

    User getUser(Long id);

    List<User> getUser(User user);
}
