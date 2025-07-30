package com.www.boot.demoes;

import com.www.demoes.DemoEsApplication;
import com.www.demoes.config.EsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoEsApplication.class)
class DemoEsApplicationTests {

	@Autowired
	private EsUtil esUtil;

	@Test
	void contextLoads() {
		boolean exist = esUtil.indexExist("jd_data");
		System.out.println("Index exist: " + exist);
	}

}
