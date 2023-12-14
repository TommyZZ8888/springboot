//package com.www.cache.ehcache.config;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.jcache.JCacheCacheManager;
//import org.springframework.cache.jcache.JCacheManagerFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//
///**
// * @Description EhcacheConfig
// * @Author 张卫刚
// * @Date Created on 2023/12/13
// */
//@Configuration
//public class EhcacheConfig {
//    @Bean
//    public JCacheManagerFactoryBean cacheManagerFactoryBean() throws Exception {
//        JCacheManagerFactoryBean factoryBean = new JCacheManagerFactoryBean();
//        factoryBean.setCacheManagerUri(getClass().getClassLoader().getResource("ehcache.xml").toURI());
//        return factoryBean;
//    }
//    @Bean
//    public CacheManager cacheManager(javax.cache.CacheManager cacheManager) {
//        JCacheCacheManager cacheCacheManager = new JCacheCacheManager();
//        cacheCacheManager.setCacheManager(cacheManager);
//        return cacheCacheManager;
//    }
//}
