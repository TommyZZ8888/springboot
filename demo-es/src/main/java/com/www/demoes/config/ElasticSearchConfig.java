package com.www.demoes.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ElasticSearchConfig {

	//	@Bean
//	public ElasticsearchClient esClient() {
//		// ES服务器URL
//		String serverUrl = "http://127.0.0.1:9200";
//		// ES用户名和密码
//		String userName = "xxx";
//		String password = "xxx";
//
////		BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
////		credsProv.setCredentials(
////				AuthScope.ANY, new UsernamePasswordCredentials(userName, password)
////		);
//
//		RestClient restClient = RestClient
//				.builder(HttpHost.create(serverUrl))
////				.setHttpClientConfigCallback(hc -> hc.setDefaultCredentialsProvider(credsProv))
//				.build();
//
//		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//		return new ElasticsearchClient(transport);
//	}
	@Bean
	public RestHighLevelClient restHighLevelClient() {
		return new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("127.0.0.1", 9200, "http")
				)
		);
	}


}
