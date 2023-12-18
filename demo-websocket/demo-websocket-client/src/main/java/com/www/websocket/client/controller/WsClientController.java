package com.www.websocket.client.controller;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description WsClientController
 * @Author 张卫刚
 * @Date Created on 2023/12/18
 */
@RestController
public class WsClientController {
    @Autowired
    WebSocketClient wsClient;

    /**
     * 客户端发消息给服务端
     * @param message
     */
    @PostMapping("/send2server")
    public void websocket(@RequestBody String message){
//        wsClient.send("test for client to server");
        wsClient.send(message);
    }

}
