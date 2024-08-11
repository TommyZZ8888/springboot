package com.www.demoproducer;

import com.www.demoproducer.producer.MyProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoProducerApplicationTests {

	@Autowired
	private MyProducer myProducer;

	@Test
	void contextLoads() {
		myProducer.send();
	}

	@Test
	void test() {
		myProducer.sendTest();
	}

}
