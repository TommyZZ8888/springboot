package com.www.zz.demoredisom.hash;

import com.redis.om.spring.annotations.Searchable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @Describtion: TestModel
 * @Author: 张卫刚
 * @Date: 2025/5/15 19:16
 */
//@RedisHash(value = "TestHashModel")
@RedisHash
@Data
public class TestHashModel {

	@Id
	private Long id;

	@Searchable
	private String title;

}
