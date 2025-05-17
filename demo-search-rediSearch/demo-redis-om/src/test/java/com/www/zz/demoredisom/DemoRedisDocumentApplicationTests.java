package com.www.zz.demoredisom;

import com.www.zz.demoredisom.dao.TestModel;
import com.www.zz.demoredisom.dao.TestRepository;
import com.www.zz.demoredisom.hash.TestHashModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
class DemoRedisDocumentApplicationTests {

	@Autowired
	private TestRepository testRepository;


	@Test
	void contextLoads() {

	}

	@Test
	void testSave() {
		TestModel testModel1 = new TestModel();
		testModel1.setId(1L);
		testModel1.setTitle("杀死一只知更鸟，毛姆作品 你好世界");
		testRepository.save(testModel1);


		TestModel testModel2 = new TestModel();
		testModel2.setId(3L);
		testModel2.setTitle("moonlight and penny from maomu , a nice author");
		testRepository.save(testModel2);

//		TestModel testModel2 = new TestModel();
//		testModel2.setId(3L);
//		testModel2.setTitle("月亮与六便士，也是毛姆的作品之一 世界面纱");
//		testRepository.save(testModel2);
//
//		TestModel testModel3 = new TestModel();
//		testModel3.setId(4L);
//		testModel3.setTitle("人性的枷锁，挣脱");
//		testRepository.save(testModel3);

	}

	@Test
	void testDel() {
		testRepository.deleteAll();
	}


	@Test
	void testFind() {
//		Optional<TestModel> testModel = testRepository.findByTitle("moonlight");
//		testModel.ifPresent(model -> System.out.println(model.getTitle()));

		List<TestModel> list = testRepository.findByTitle("毛姆");
		for (TestModel model : list) {
			System.out.println(model.getTitle());
		}
	}

}
