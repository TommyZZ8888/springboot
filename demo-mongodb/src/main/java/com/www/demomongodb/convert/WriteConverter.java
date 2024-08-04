package com.www.demomongodb.convert;

import com.www.demomongodb.model.Users;
import org.springframework.core.convert.converter.Converter;

import org.bson.Document;

/**
 * @Describtion: WriteConvert
 * @Author: 张卫刚
 * @Date: 2024/8/4 20:48
 */
public class WriteConverter implements Converter<Users, Document> {

	@Override
	public Document convert(Users source) {
		Document document = new Document();
		document.put("_id", source.getId());
		document.put("name", source.getName());
		document.put("age", source.getAge() + 1);
		return document;
	}
}