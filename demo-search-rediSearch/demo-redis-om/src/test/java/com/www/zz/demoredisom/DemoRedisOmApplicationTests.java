package com.www.zz.demoredisom;

import com.redis.om.spring.ops.RedisModulesOperations;
import com.www.zz.demoredisom.hash.TestHashModel;
import com.www.zz.demoredisom.hash.TestHashRepository;
import io.redisearch.FieldName;
import io.redisearch.Schema;
import io.redisearch.client.Client;
import io.redisearch.client.IndexDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;


@SpringBootTest
class DemoRedisOmApplicationTests {

	@Autowired
	private RedisModulesOperations<String> redisModulesOperations;

	@Autowired
	private TestHashRepository testRepository;

	@Test
	void contextLoads() {
		try {
			redisModulesOperations.opsForSearch("com.www.zz.demoredisom.hash.TestHashModelIdx").getInfo();
			redisModulesOperations.opsForSearch("com.www.zz.demoredisom.hash.TestHashModelIdx").dropIndex();

			Schema.Field titleField = new Schema.Field(FieldName.of("title"), Schema.FieldType.FullText);
			Schema.Field idField = new Schema.Field(FieldName.of("id"), Schema.FieldType.Numeric);
			Schema schema = new Schema()
					.addField(titleField)
					.addField(idField);

			IndexDefinition indexDefinition = new IndexDefinition(IndexDefinition.Type.HASH).setLanguage("chinese");
			redisModulesOperations.opsForSearch("com.www.zz.demoredisom.hash.TestHashModelIdx").createIndex(
					schema,
					Client.IndexOptions.defaultOptions().setDefinition(indexDefinition)
			);
			System.out.println("Index already exists, skipping creation.");
		} catch (Exception e) {
			// 创建索引，设置 default_language 为 chinese
			Schema.Field titleField = new Schema.Field(FieldName.of("title"), Schema.FieldType.FullText);
			Schema.Field idField = new Schema.Field(FieldName.of("id"), Schema.FieldType.Numeric);
			Schema schema = new Schema()
					.addField(titleField)
					.addField(idField);

			IndexDefinition indexDefinition = new IndexDefinition(IndexDefinition.Type.HASH).setLanguage("chinese");
			redisModulesOperations.opsForSearch("com.www.zz.demoredisom.hash.TestHashModelIdx").createIndex(
					schema,
					Client.IndexOptions.defaultOptions().setDefinition(indexDefinition)
			);
//			redisModulesOperations.opsForSearch("com.www.zz.demoredisom.hash.TestHashModelIdx").createIndex(
//					"idx:TestHashModel", // 索引名称，通常为 idx:<prefix>
//					"HASH",              // 键类型为 HASH
//					new String[]{"TestHashModel:"}, // 前缀
//					"chinese",           // 设置默认语言为中文
//					new String[]{"title", "TEXT", "WEIGHT", "1.0"} // 字段定义
//			);
			System.out.println("Index created with Chinese language support.");
		}
	}

	@Test
	void testSave() {
		TestHashModel testModel1 = new TestHashModel();
		testModel1.setId(2L);
		testModel1.setTitle("杀死一只知更鸟，毛姆作品 你好世界");
		testRepository.save(testModel1);

//		TestModel testModel2 = new TestModel();
//		testModel2.setId(3L);
//		testModel2.setTitle("月亮与六便士，也是毛姆的作品之一 世界面纱");
//		testRepository.save(testModel2);
//
//		TestModel testModel3 = new TestModel();
//		testModel3.setId(4L);
//		testModel3.setTitle("人性的枷锁，挣脱");
//		testRepository.save(testModel3);


		TestHashModel testModel2 = new TestHashModel();
		testModel2.setId(3L);
		testModel2.setTitle("moonlight and penny from maomu , a nice author");
		testRepository.save(testModel2);

		TestHashModel testModel3 = new TestHashModel();
		testModel3.setId(4L);
		testModel3.setTitle("break free, also from maomu, a nice author");
		testRepository.save(testModel3);
	}

	@Test
	void testDel() {
		testRepository.deleteAll();
	}

	@Test
	void testFind() {
//		Iterable<TestHashModel> all = testRepository.findAll();
//		for (TestHashModel testModel : all) {
//			System.out.println(testModel.getId() + " " + testModel.getTitle());
//		}
//		Optional<TestHashModel> testModel2 = testRepository.findById(2L);
//		System.out.println(testModel2.get().getId() + " " + testModel2.get().getTitle());

		// 默认支持英文分词
//		Optional<TestHashModel> testModel = testRepository.findByTitle("author penny");
//		System.out.println(testModel.get().getId() + " " + testModel.get().getTitle());

//		List<TestHashModel> testModels = testRepository.findByTitleContaining("author");
//		for (TestHashModel model : testModels) {
//			System.out.println(model.getId() + " " + model.getTitle());
//		}

		List<TestHashModel> testModels = testRepository.findByTitle("知更鸟");
		for (TestHashModel model : testModels) {
			System.out.println(model.getId() + " " + model.getTitle());
		}

	}

}
