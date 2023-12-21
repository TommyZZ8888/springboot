package com.www.config.consul.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description DBConfig
 * @Author 张卫刚
 * @Date Created on 2023/12/21
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DbConfig {

    /**连接地址*/
    private String url ;
    /**用户名*/
    private String username;
    /**密码*/
    private String password;

    public DbConfig() {
    }

    public DbConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
