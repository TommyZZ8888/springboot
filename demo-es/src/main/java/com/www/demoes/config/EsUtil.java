package com.www.demoes.config;

import com.www.demoes.domain.JdData;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EsUtil {

	private static final Logger logger = LoggerFactory.getLogger(EsUtil.class);

	private static final String indexName = "jd_data";
	private final ObjectMapper objectMapper = new ObjectMapper(); // 用于对象-JSON 转换

	@Autowired
	private RestHighLevelClient client;

	/**
	 * 判断索引是否存在
	 */
	public boolean indexExist(String index) {
		try {
			GetIndexRequest request = new GetIndexRequest(index);
			return client.indices().exists(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			logger.error("检查索引 {} 是否存在失败: {}", index, e.getMessage(), e);
			throw new RuntimeException("检查索引失败", e);
		}
	}

	/**
	 * 创建索引并设置 mapping
	 */
	public void createIndex(String index) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(index);
		request.settings(Settings.builder()
				.put("index.number_of_shards", 1)
				.put("index.number_of_replicas", 1)
		);
		request.mapping("{\n" +
				"  \"properties\": {\n" +
				"    \"id\": {\"type\": \"long\"},\n" +
				"    \"title\": {\"type\": \"text\", \"index\": false}\n" +
				"  }\n" +
				"}", XContentType.JSON);

		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
		logger.info("索引创建响应: acknowledged = {}", response.isAcknowledged());
	}

	/**
	 * 插入或更新一条数据
	 */
	public IndexResponse insertOrUpdateOne(String index, JdData entity) throws IOException {
		if (entity == null || entity.getId() == null || entity.getTitle() == null) {
			logger.warn("插入数据非法，跳过：{}", entity);
			return null;
		}
		IndexRequest request = new IndexRequest(index)
				.id(entity.getId().toString())
				.source(objectMapper.writeValueAsString(entity), XContentType.JSON);

		return client.index(request, RequestOptions.DEFAULT);
	}

	/**
	 * 根据 ID 查询
	 */
	public JdData search(String index, String id) throws IOException {
		GetRequest request = new GetRequest(index, id);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);

		if (response.isExists()) {
			return objectMapper.readValue(response.getSourceAsString(), JdData.class);
		} else {
			logger.warn("未找到 ID 为 {} 的数据", id);
			return null;
		}
	}

	/**
	 * 批量插入数据
	 */
	public BulkResponse insertBatch(String index, List<JdData> list) throws IOException {
		if (list == null || list.isEmpty()) {
			logger.warn("批量插入失败：数据为空");
			return null;
		}

		BulkRequest bulkRequest = new BulkRequest();

		for (JdData data : list) {
			if (data.getId() != null && data.getTitle() != null) {
				IndexRequest request = new IndexRequest(index)
						.id(data.getId().toString())
						.source(objectMapper.writeValueAsString(data), XContentType.JSON);
				bulkRequest.add(request);
			}
		}

		return client.bulk(bulkRequest, RequestOptions.DEFAULT);
	}

	/**
	 * 删除索引
	 */
	public void deleteIndex(String index) {
		try {
			DeleteIndexRequest request = new DeleteIndexRequest(index);
			AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
			if (response.isAcknowledged()) {
				logger.info("索引 {} 删除成功", index);
			} else {
				logger.warn("索引 {} 删除失败", index);
			}
		} catch (IOException e) {
			logger.error("删除索引 {} 失败: {}", index, e.getMessage(), e);
			throw new RuntimeException("删除索引失败", e);
		}
	}
}
