package com.www.demomongodb.convert;

import com.www.demomongodb.model.Users;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

/**
 * @Describtion: WriteConvert
 * @Author: 张卫刚
 * @Date: 2024/8/4 20:48
 */
public class ReadConverter implements Converter<Document, Users> {

	public Users convert(Document source) {
		Users user = new Users();
		user.setName(source.get("name")+"a");
		user.setAge((Integer) source.get("age"));
		return user;
	}
}