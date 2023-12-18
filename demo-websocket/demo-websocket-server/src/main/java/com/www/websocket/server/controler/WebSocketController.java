package com.www.websocket.server.controler;

import com.www.websocket.server.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description WebSocketController
 * @Author 张卫刚
 * @Date Created on 2023/12/18
 */
@RestController
public class WebSocketController {

    @Autowired
private WebSocketServer websocketService;

    @PostMapping("/send2client")
    public void send2Client(@RequestBody String message){
//        wsServer.sendMessageToAll("this is a test for server to client");
        websocketService.sendMessageByStr(message);
    }
}
