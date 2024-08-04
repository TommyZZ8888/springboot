package com.www.demomongodb.repository;

import com.www.demomongodb.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Describtion: UserRepository
 * @Author: 张卫刚
 * @Date: 2024/8/4 10:42
 */
public interface UserRepository extends MongoRepository<Users, String> {

    List<Users> findByNameLike(String name);
}
