package com.www.cache.ehcache.service;

import com.www.cache.ehcache.entity.User;

/**
 * @Description UserService
 * @Author 张卫刚
 * @Date Created on 2023/12/13
 */
public interface UserService {

    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    User saveOrUpdate(User user);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);
}
