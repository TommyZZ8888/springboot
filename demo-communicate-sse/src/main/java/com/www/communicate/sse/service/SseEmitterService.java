package com.www.communicate.sse.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.www.communicate.sse.model.SseEmitterData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Description SseEmitterService
 * @Author 张卫刚
 * @Date Created on 2023/12/14
 */
@Service
@Slf4j
public class SseEmitterService {

    public static final Map<String, SseEmitter> SSE_CACHE = new ConcurrentHashMap<>();


    public SseEmitter getSseEmitterByClientId(String clientId) {
        return SSE_CACHE.get(clientId);
    }

    /**
     * 创建连接
     * @param clientId
     * @return
     */
    public SseEmitter createConnect(String clientId) {
        // 设置超时时间，0代表不过期，默认30s，超时未完成会抛出异常: AsyncRequest
        SseEmitter sseEmitter = new SseEmitter(0L);
        if (StrUtil.isBlank(clientId)) {
            clientId = IdUtil.simpleUUID();
        }
        //回调
        sseEmitter.onCompletion(completionCallBack(clientId));
        sseEmitter.onTimeout(timeOutCallBack(clientId));
        sseEmitter.onError(errorCallBack(clientId));
        SSE_CACHE.put(clientId, sseEmitter);
        log.info("创建新的sse连接，当前用户：{}   累计用户：{}", clientId, SSE_CACHE);
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(HttpStatus.HTTP_CREATED))
                    .data(clientId, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            log.error("创建长链接异常，客户端ID:{}   异常信息:{}", clientId, e.getMessage());
        }
        return sseEmitter;
    }

    /**
     * 给所有客户端发送消息
     * @param msg
     */
    public void sendMessageToAllClient(String msg) {
        if (MapUtil.isEmpty(SSE_CACHE)) {
            return;
        }
        for (Map.Entry<String, SseEmitter> entry : SSE_CACHE.entrySet()) {
            SseEmitterData sseEmitterData = new SseEmitterData();
            sseEmitterData.setClientId(entry.getKey());
            sseEmitterData.setName(msg);
            sseEmitterData.setData(msg);
            sendMsgToClientByClientId(entry.getKey(), sseEmitterData, entry.getValue());
        }
    }

    /**
     * 指定客户端发送消息
     * @param clientId
     * @param msg
     */
    public void sendMessageToOneClient(String clientId, String msg) {
        SseEmitterData sseEmitterData = new SseEmitterData(clientId, msg, msg);
        sendMsgToClientByClientId(clientId, sseEmitterData, getSseEmitterByClientId(clientId));
    }

    /**
     * 关闭连接
     * @param clientId
     */
    public void closeConnect(String clientId) {
        SseEmitter sseEmitter = getSseEmitterByClientId(clientId);
        if (ObjectUtil.isNotNull(sseEmitter)) {
            sseEmitter.complete();
            removeUser(clientId);
        }
    }


    public void sendMsgToClientByClientId(String clientId, SseEmitterData sseEmitterData, SseEmitter sseEmitter) {
        if (ObjectUtil.isNull(sseEmitter)) {
            log.error("推送消息失败：客户端{}未创建长链接,失败消息:{}",
                    clientId, sseEmitterData.toString());
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(String.valueOf(HttpStatus.HTTP_OK))
                .data(sseEmitterData, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (Exception e) {
            log.error("推送消息失败：{},尝试进行重推", sseEmitterData.toString());
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(5000);
                    sseEmitter = getSseEmitterByClientId(clientId);
                    if (ObjectUtil.isNull(sseEmitter)) {
                        log.error("{}的第{}次消息重推失败，未创建长链接", clientId, i + 1);
                        continue;
                    }
                    sseEmitter.send(sendData);
                } catch (Exception ex) {
                    log.error("{}的第{}次消息重推失败", clientId, i + 1, ex);
                    continue;
                }
                log.info("{}的第{}次消息重推成功,{}", clientId, i + 1, sseEmitterData.toString());
                return;
            }
        }
    }

    /**
     * 长链接完成后回调接口（即关闭连接时调用）
     * @param clientId
     * @return
     */
    private Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("结束连接：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 连接超时调用
     * @param clientId
     * @return
     */
    private Runnable timeOutCallBack(String clientId) {
        return () -> {
            log.info("连接超时：{}", clientId);
            removeUser(clientId);
        };
    }

    /**
     * 推送异常时回调方法
     * @param clientId
     * @return
     */
    private Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("SseEmitterService[errorCallBack]: 连接异常，客户端id：{}", clientId);

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(5000);
                    SseEmitter sseEmitter = getSseEmitterByClientId(clientId);
                    if (ObjectUtil.isNull(sseEmitter)) {
                        log.error("SseEmitterServiceImpl[errorCallBack]：第{}次消息重推失败,未获取到 {} 对应的长链接", i + 1, clientId);
                        continue;
                    }
                    sseEmitter.send("失败后重新推送");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void removeUser(String clientId) {
        SSE_CACHE.remove(clientId);
        log.info("SseEmitterService[removeUser]:移除用户：{}", clientId);
    }

}
