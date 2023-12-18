package com.www.websocket.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Description 该配置类用于创建ServerEndpoint注解，此注解用在类上，使得此类成为服务端websocket
 * @Author 张卫刚
 * @Date Created on 2023/12/18
 */
@Configuration
public class WebsocketConfig {

    @Bean
public ServerEndpointExporter serverEndpointExporter(){
    return new ServerEndpointExporter();
}

}
