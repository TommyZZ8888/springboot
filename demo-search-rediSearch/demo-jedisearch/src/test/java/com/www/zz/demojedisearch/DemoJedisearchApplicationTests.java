package com.www.zz.demojedisearch;

import com.www.zz.demojedisearch.model.TestUser;
import com.www.zz.demojedisearch.utils.MyBeanUtil;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
class DemoJedisearchApplicationTests {

	private final Client client = new Client("myIdx", "192.168.30.200", 6379);
	;

	private static final String idxName = "myIdx";

	private static final String prefix = "user:";

	private static final String language = "chinese";


	@Test
	void contextLoads() {
	}


	@Test
	void testInfoIndex() {
		Map<String, Object> map = client.getInfo();
		map.forEach((k, v) -> System.out.println(k + ":" + v));
	}

	@Test
	void testAddDocument() {
		Map<String, Object> hash = new HashMap<>();
		hash.put("id", 1);
		hash.put("name", "张三");
		hash.put("_language", language);

		client.addDocument(prefix + "1", hash);
	}

	@Test
	void testSearch() {
		Query query = new Query();
		query.setLanguage(language);
		query.limit(0, 10000);
		SearchResult searchResult = client.search(query);

		List<TestUser> users = searchResult.docs.stream()
				.filter(Objects::nonNull)
				.map(doc -> MyBeanUtil.propertiesToBean(doc.getProperties(), TestUser.class))
				.collect(Collectors.toList());

		for (TestUser user : users) {
			System.out.println(user.getId() + ":" + user.getName());
		}
	}

	private void printResult(SearchResult searchResult){
		List<TestUser> users = searchResult.docs.stream()
				.filter(Objects::nonNull)
				.map(doc -> MyBeanUtil.propertiesToBean(doc.getProperties(), TestUser.class))
				.collect(Collectors.toList());

		for (TestUser user : users) {
			System.out.println(user.getId() + ":" + user.getName());
		}
	}

// =============================================中文分词测试==============================

	/**
	 * 测试中文分词
	 */
	@Test
	void testSearchFenCi() {
		String queryKey2 = String.format("@name:(%s)", "作家毛姆");
		Query query2 = new Query(queryKey2);
		query2.setLanguage(language);
		SearchResult searchResult2 = client.search(query2);
		printResult(searchResult2);
	}

	@Test
	void testSearchFenCi2() {
		String queryKey = String.format("@name:(%s)", "上海第六");
		Query query = new Query(queryKey);
		query.setLanguage(language);
		SearchResult searchResult = client.search(query);
		printResult(searchResult);
		System.out.println("======================================");

		String queryKey2 = String.format("@name:(%s)", "上海市第六");
		Query query2 = new Query(queryKey2);
		query2.setLanguage(language);
		SearchResult searchResult2 = client.search(query2);
		printResult(searchResult2);
		System.out.println("======================================");

		String queryKey3 = String.format("@name:(%s)", "附属医院");
		Query query3 = new Query(queryKey3);
		query3.setLanguage(language);
		SearchResult searchResult3 = client.search(query3);
		printResult(searchResult3);
	}

	@Test
	void testSearchFenCi3() {
		String queryKey = String.format("@name:(%s)", "上,海,第,六");
		Query query = new Query(queryKey);
		query.setLanguage(language);
		SearchResult searchResult = client.search(query);
		printResult(searchResult);
	}


	@Test
	void testDelDocument() {
		boolean exists = client.deleteDocument(prefix + "1");

	}
}
