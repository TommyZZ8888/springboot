//package com.www.zz.demojedisearch.config;
//
//import io.redisearch.client.Client;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Describtion: RsClientConfig
// * @Author: 张卫刚
// * @Date: 2025/5/24 15:25
// */
//@Configuration
//public class RsClientConfig {
//	@Bean
//	public Client client(){
//        /*
//            Client 是主要的 RediSearch 客户端类，包装了连接管理和所有 RediSearch 命令
//            连接redisearch
//            "store"索引(相当于表)
//         */
//		Client client = new Client("myIdx", "192.168.30.200", 6379);
//		return client;
//	}
//}