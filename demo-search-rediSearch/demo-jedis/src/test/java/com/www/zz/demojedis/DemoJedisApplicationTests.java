package com.www.zz.demojedis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.UnifiedJedis;

import java.util.Set;

@SpringBootTest
class DemoJedisApplicationTests {

	@Autowired
	private UnifiedJedis client;

	@Test
	void contextLoads() {

	}


	@Test
	void testSet() {
		Set<String> strings = client.ftList();
        strings.forEach(System.out::println);
	}



}
