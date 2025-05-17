package com.www.zz.demoredisom.dao;

import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Describtion: TestRepository
 * @Author: 张卫刚
 * @Date: 2025/5/15 19:24
 */
public interface TestRepository extends RedisDocumentRepository<TestModel, Long> {

	List<TestModel> findByTitle(String title);

	List<TestModel> findByTitleContaining(String title);

	List<TestModel> findByTitleLike(String title);

}
