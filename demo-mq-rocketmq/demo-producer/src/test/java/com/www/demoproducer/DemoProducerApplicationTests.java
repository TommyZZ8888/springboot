package com.www.demoproducer;

import com.www.demoproducer.producer.MyProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SpringBootTest
class DemoProducerApplicationTests {

	@Autowired
	private MyProducer myProducer;

	@Test
	void contextLoads() {
		myProducer.send();
	}

	@Test
	void test() throws IllegalAccessException {
		myProducer.sendTest();
		Class<?> clazz = myProducer.getClass();

		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			method.setAccessible(true);
		}
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(clazz, "11");
		}
	}

}
