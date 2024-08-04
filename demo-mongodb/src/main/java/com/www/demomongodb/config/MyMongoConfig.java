package com.www.demomongodb.config;

import com.www.demomongodb.convert.ReadConverter;
import com.www.demomongodb.convert.WriteConverter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * @Describtion: MyMongoConfig
 * @Author: 张卫刚
 * @Date: 2024/8/4 20:53
 */
class MyMongoConfig extends AbstractMongoClientConfiguration {

	@Override
	public String getDatabaseName() {
		return "testdb";
	}
	@Override
	protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter adapter) {
		adapter.registerConverter(new ReadConverter());
		adapter.registerConverter(new WriteConverter());
	}

}
