package com.www.orm.jpa.repository;

import com.www.orm.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description UserDao
 * @Author 张卫刚
 * @Date Created on 2023/11/30
 */
@Repository
public interface UserDao extends JpaRepository<User,Long> {
}
