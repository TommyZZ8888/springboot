package com.www.cache.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoCacheEhcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCacheEhcacheApplication.class, args);
    }

}
