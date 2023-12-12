package com.www.cache.redis.service;

import com.www.cache.redis.entity.User;

/**
 * @Description UserService
 * @Author 张卫刚
 * @Date Created on 2023/12/12
 */
public interface UserService {

    User saveOrUpdate(User user);

    User get(Long id);

    void delete(Long id);
}
