package com.www.zz.demoredisom.hash;

import com.redis.om.spring.annotations.Query;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Describtion: TestRepository  详细可用api 参考 src/main/resources/images/img.png
 * @Author: 张卫刚
 * @Date: 2025/5/15 19:24
 */
public interface TestHashRepository extends CrudRepository<TestHashModel, Long> {

	List<TestHashModel> findByTitle(String title);

	List<TestHashModel> findByTitleContaining(String title);

	List<TestHashModel> findByTitleLike(String title);


}
