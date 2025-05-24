package com.www.zz.demojedis;

import com.www.zz.demojedis.constants.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class DemoJedisApplicationTests {

	@Autowired
	private UnifiedJedis client;

	private static final String idxName = "myIdx";

	private static final String prefix = "user:";

	private static final String language = "chinese";


	@Test
	void contextLoads() {

	}

	@Test
	void testFTList() {
		Set<String> strings = client.ftList();
        strings.forEach(System.out::println);
	}

	@Test
	void testCreateIndex() {
			IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.HASH)
					.setPrefixes(prefix)
					.setLanguage(language);
		Schema schema = new Schema()
				.addNumericField("id")
				.addTextField("name", 1.0);
			client.ftCreate(idxName,
					IndexOptions.defaultOptions().setDefinition(rule),
					schema);
	}


	@Test
	void testInfoIndex() {
		Map<String, Object> map = client.ftInfo(idxName);
		map.forEach((k, v) -> System.out.println(k + ":" + v));
	}

	@Test
	void testAddDocument() {
		Map<String, String> hash = new HashMap<>();
		hash.put("id", "1");
		hash.put("name", "张三");
		hash.put("_language", Constants.GOODS_IDX_LANGUAGE);

		client.hset(prefix + "1", hash);
	}

	@Test
	void testSearch() {
		Query query = new Query();
		query.addParam("name", "张三");
		query.setLanguage(Constants.GOODS_IDX_LANGUAGE);
		query.limit(0, 10000);
		SearchResult searchResult = client.ftSearch(idxName, query);
		searchResult.getDocuments().forEach(System.out::println);
	}

// =============================================中文分词测试==============================
	/**
	 * 测试中文分词
	 */
	@Test
	void testSearchFenCi() {
//		Map<String, String> hash = new HashMap<>();
//		hash.put("id", "2");
//		hash.put("name", "毛姆是二流作家，作品面纱");
//		hash.put("_language", Constants.GOODS_IDX_LANGUAGE);
//		client.hset(prefix + "2", hash);
//
//		Map<String, String> hash2 = new HashMap<>();
//		hash2.put("id", "3");
//		hash2.put("name", "毛姆著作还有月亮与六便士");
//		hash2.put("_language", Constants.GOODS_IDX_LANGUAGE);
//		client.hset(prefix + "3", hash2);



//		String queryKey = String.format("@name:(%s)", "毛姆月亮");
//		Query query = new Query(queryKey);
//		query.setLanguage(language);
//		SearchResult searchResult = client.ftSearch(idxName, query);
//		searchResult.getDocuments().forEach(System.out::println);
//		System.out.println("======================================");


		String queryKey2 = String.format("@name:(%s)", "作家毛姆");
		Query query2 = new Query(queryKey2);
		query2.setLanguage(language);
		SearchResult searchResult2 = client.ftSearch(idxName, query2);
		searchResult2.getDocuments().forEach(System.out::println);
	}

	@Test
	void testSearchFenCi2() {
//		Map<String, String> hash = new HashMap<>();
//		hash.put("id", "4");
//		hash.put("name", "上海市第六人民医院");
//		hash.put("_language", Constants.GOODS_IDX_LANGUAGE);
//		client.hset(prefix + "4", hash);
//
//		Map<String, String> hash2 = new HashMap<>();
//		hash2.put("id", "5");
//		hash2.put("name", "上海交通大学医学院附属瑞金医院");
//		hash2.put("_language", Constants.GOODS_IDX_LANGUAGE);
//		client.hset(prefix + "5", hash2);
//
//		Map<String, String> hash3 = new HashMap<>();
//		hash3.put("id", "6");
//		hash3.put("name", "上海交通大学医学院附属新华医院");
//		hash3.put("_language", Constants.GOODS_IDX_LANGUAGE);
//		client.hset(prefix + "6", hash3);

		String queryKey = String.format("@name:(%s)", "上海第六");
		Query query = new Query(queryKey);
		query.setLanguage(language);
		SearchResult searchResult = client.ftSearch(idxName, query);
		searchResult.getDocuments().forEach(System.out::println);
		System.out.println("======================================");

		String queryKey2 = String.format("@name:(%s)", "上海市第六");
		Query query2 = new Query(queryKey2);
		query2.setLanguage(language);
		SearchResult searchResult2 = client.ftSearch(idxName, query2);
		searchResult2.getDocuments().forEach(System.out::println);
		System.out.println("======================================");

		String queryKey3 = String.format("@name:(%s)", "附属医院");
		Query query3 = new Query(queryKey3);
		query3.setLanguage(language);
		SearchResult searchResult3 = client.ftSearch(idxName, query3);
		searchResult3.getDocuments().forEach(System.out::println);
	}


	@Test
	void testDelDocument() {
		boolean exists = client.exists(prefix+ "1");
		if (exists){
			long hdel = client.del(prefix + "1");
			System.out.println(hdel);
		}
	}

}
