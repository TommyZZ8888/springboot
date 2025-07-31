package com.www.boot.demoes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.demoes.DemoEsApplication;
import com.www.demoes.config.EsUtil;
import com.www.demoes.domain.JdData;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = DemoEsApplication.class)
class DemoEsApplicationTests {

	@Autowired
	private EsUtil esUtil;

	@Autowired
	private RestHighLevelClient client;

	private final String indexName = "jd_data";

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() {
		boolean exist = esUtil.indexExist("jd_data");
		System.out.println("Index exist: " + exist);
	}

	@Test
	void testInsert() throws IOException {
		JdData jdData = new JdData();
		jdData.setId(12L);
		jdData.setTitle("这是一段测试数据");
		IndexRequest request = new IndexRequest(indexName)
				.id(jdData.getId().toString())
				.source(objectMapper.writeValueAsString(jdData), XContentType.JSON);
		client.index(request, RequestOptions.DEFAULT);
	}

}
