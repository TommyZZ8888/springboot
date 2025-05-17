package com.www.zz.demoredisom.dao;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Searchable;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Describtion: TestModel
 * @Author: 张卫刚
 * @Date: 2025/5/15 19:16
 */
@Document(language = "chinese")
@Data
public class TestModel {

	@Id
	private Long id;

	@Searchable
	private String title;

}
