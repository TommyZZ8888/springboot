package com.www.search.rediSearch;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set; /**
 * @Describtion: RediSearchDemo
 * @Author: 张卫刚
 * @Date: 2025/3/17 19:19
 */
public class RediSearchDemo {
	public static final String GOODS_IDX_PREFIX = "idx:goods:";


	@Autowired
	private UnifiedJedis client;

	public RediSearchDemo() {
		GenericObjectPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(10);
		jedisPoolConfig.setMaxWaitMillis(3000);
		jedisPoolConfig.setJmxEnabled(false);
		client = new JedisPooled(jedisPoolConfig, "192.168.30.200", 6379, 0);
	}

	/**
	 * 新增索引数据
	 */
	private void hset(String keyPrefix, Map<String, String> hash) {
		// 支持中文
		hash.put("_language", "chinese");
		client.hset(keyPrefix, hash);
	}
	/**
	 * 查询索引列表
	 */
	public Set<String> listIndex() {
		return client.ftList();
	}


	/**
	 * 创建索引
	 *
	 * @param idxName 索引名称
	 * @param prefix  要索引的数据前缀
	 * @param schema  索引字段配置
	 */
	public void createIndex(String idxName, String prefix, Schema schema) {
		IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.HASH)
				.setPrefixes(prefix)
				.setLanguage("chinese");
		client.ftCreate(idxName,
				IndexOptions.defaultOptions().setDefinition(rule),
				schema);
	}

	/**
	 * 查询
	 *
	 * @param idxName 索引名称
	 * @param search  查询key
	 * @param sort    排序字段
	 * @return searchResult
	 */
	public SearchResult query(String idxName, String search, String sort) {
		Query q = new Query(search);
		if (StringUtils.isNotBlank(sort)) {
			q.setSortBy(sort, false);
		}
		q.setLanguage("chinese");
		q.limit(0, 10);
		return client.ftSearch(idxName, q);
	}


	public static void main(String[] args) {
		RediSearchDemo example = new RediSearchDemo();
		String id ="1";
		Map<String, String> hash = new HashMap<>();
		hash.put("id",id);
		hash.put("goodsName","你好hello");
		example.hset("idx:goods" , hash );
		SearchResult searchResult = example.query("idx:goods","*", null);
		System.out.println(searchResult.toString());
	}

}

