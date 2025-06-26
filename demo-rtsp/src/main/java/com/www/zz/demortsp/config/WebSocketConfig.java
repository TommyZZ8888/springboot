package com.www.zz.demortsp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 
 * * @projectName videoservice
 * * @title   WebSocketConfig 
 * * @package  com.de.config 
 * * @description  开启websocket支持
 * * @author IT_CREAT  
 * * @date 2020 2020/4/12 0012 下午 16:39 
 * * @version V1.0.0
 * 
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
