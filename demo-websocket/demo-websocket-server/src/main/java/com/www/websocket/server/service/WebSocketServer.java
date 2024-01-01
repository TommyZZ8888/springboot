package com.www.websocket.server.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.micrometer.common.util.StringUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description WebsocketService
 * @Author 张卫刚
 * @Date Created on 2023/12/18
 */
@Slf4j
@Component
@ServerEndpoint("/websocketService")
public class WebSocketServer {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    public static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.userId = session.getId();
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            webSocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
        HashMap<String, String> data = new HashMap<>();
        data.put("userId", userId);
        sendMessageByStr(JSON.toJSONString(data));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息,必须是json串
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            //解析发送的报文
            JSONObject jsonObject = JSON.parseObject(message);
            //追加发送人(防止串改)
            if (jsonObject != null) {
                jsonObject.put("fromUserId", this.userId);
                String toUserId = jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessageByStr(jsonObject.toJSONString());
                } else {
                    log.error("请求的userId:" + toUserId + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        log.error("websocket error: ", error);
    }

    public void sendMessageByStr(String message) {
        if (StringUtils.isNotBlank(message)) {
            try {
                if (this.session.isOpen()) {
                    this.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                log.error("发送到用户：" + this.userId + "信息失败 ，信息是：" + message);
                log.error("websocket send str msg exception: ", e);
            }
        }
    }

    public void sendMessageByObject(Object message) {
        if (message != null) {
            try {
                synchronized (this.session) {
                    this.session.getBasicRemote().sendObject(message);
                }
            } catch (IOException | EncodeException e) {
                log.error("发送到用户：" + this.userId + "信息失败 ，信息是：" + message);
                log.error("websocket send object msg exception: ", e);
            }
        }
    }

    public void sendBinary(ByteBuffer message) {
        if (message != null) {
            try {
                this.session.getBasicRemote().sendBinary(message);
            } catch (IOException e) {
                log.error("发送到用户：" + this.userId + "信息失败 ，信息是：" + message);
                log.error("websocket send byteBuffer msg exception: ", e);
            }
        }
    }

    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, String userId) {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessageByStr(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * 向所有的客户端发送消息
     * @param byteBuffer byteBuffer
     * @throws IOException IOException
     */
    public static void sendAllByBinary(ByteBuffer byteBuffer) {
        if (!webSocketMap.isEmpty()) {
            Collection<WebSocketServer> values = webSocketMap.values();
            for (WebSocketServer next : values) {
                next.sendBinary(byteBuffer);
            }
        }
    }

    public static void sendAllByObject(Object message) {
        if (!webSocketMap.isEmpty()) {
            Collection<WebSocketServer> values = webSocketMap.values();
            for (WebSocketServer next : values) {
                next.sendMessageByObject(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
