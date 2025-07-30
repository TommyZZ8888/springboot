package com.www.demoes.config;

/**
 * @Describtion: EsClientConfig
 * @Author: 张卫刚
 * @Date: 2025/7/30 16:39
 */

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch RestHighLevelClient 配置类
 * Spring Boot 2.x 提供了对 RestHighLevelClient 的自动配置，通常我们无需手动创建连接池。
 * 如果需要更细粒度的控制，可以通过 RestClientBuilderCustomizer 进行定制。
 */
@Configuration
//@ConditionalOnProperty(prefix = "es", name = "enable", havingValue = "true") // 只有当 es.enable=true 时才加载此配置
public class EsClientConfig {

	// Spring Boot 会自动从 application.yml/properties 读取这些配置
	@Value("${spring.elasticsearch.uris}")
	private String uris;

	@Value("${spring.elasticsearch.connection-timeout:50s}") // 默认值
	private String connectionTimeout;

	@Value("${spring.elasticsearch.socket-timeout:60s}") // 默认值
	private String socketTimeout;

	/**
	 * 创建 RestHighLevelClient Bean
	 * Spring Boot 会自动检测并使用这个 Bean。
	 * 无需手动管理连接池，Spring Boot 会处理客户端的生命周期。
	 *
	 * 如果 Spring Boot 版本较新，且想使用新的 Java API Client (Elasticsearch 7.15+ / 8.x)，
	 * 可以配置 org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
	 * 并注入 ElasticsearchClient。
	 *
	 * @return RestHighLevelClient 实例
	 */
	@Bean
	public RestHighLevelClient restHighLevelClient() {
//		HttpHost[] httpHosts = Arrays.stream(new String[]{uris})
//				.map(uri -> {
//					String[] parts = uri.split(":");
//					String scheme = parts[0];
//					String host = parts[1].substring(2); // 去掉 "//"
//					int port = Integer.parseInt(parts[2]);
//					return new HttpHost(host, port, scheme);
//				})
//				.toArray(HttpHost[]::new);
		String[] parts = uris.split(":");
		HttpHost httpHost = new HttpHost(parts[0], Integer.parseInt(parts[1]), "http");

		RestClientBuilder builder = RestClient.builder(httpHost);

		// 设置超时时间
		builder.setRequestConfigCallback(requestConfigBuilder -> {
			requestConfigBuilder.setConnectTimeout(parseDuration(connectionTimeout));
			requestConfigBuilder.setSocketTimeout(parseDuration(socketTimeout));
			return requestConfigBuilder;
		});

		// 如果需要配置连接池大小，可以通过 RestClientBuilder 自定义，
		// 但通常 Spring Boot 默认的连接管理足以满足大部分需求，
		// 或者考虑使用 Apache Http Async Client 的连接池配置。
		// 对于 RestHighLevelClient，底层是 RestClient，其连接池是基于 Apache Http Async Client 的。
		// 具体配置可以参考：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low-level-tuning.html

		return new RestHighLevelClient(builder);
	}

	/**
	 * 解析持续时间字符串（例如 "50s" -> 50000 毫秒）
	 * @param durationString
	 * @return
	 */
	private int parseDuration(String durationString) {
		if (durationString == null || durationString.isEmpty()) {
			return 0;
		}
		durationString = durationString.toLowerCase();
		if (durationString.endsWith("ms")) {
			return Integer.parseInt(durationString.substring(0, durationString.length() - 2));
		} else if (durationString.endsWith("s")) {
			return Integer.parseInt(durationString.substring(0, durationString.length() - 1)) * 1000;
		} else if (durationString.endsWith("m")) {
			return Integer.parseInt(durationString.substring(0, durationString.length() - 1)) * 60 * 1000;
		}
		return Integer.parseInt(durationString); // 假设是毫秒
	}
}