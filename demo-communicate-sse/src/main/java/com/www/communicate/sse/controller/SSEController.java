package com.www.communicate.sse.controller;

import cn.hutool.core.util.RandomUtil;
import com.www.communicate.sse.model.SseEmitterData;
import com.www.communicate.sse.service.SseEmitterService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("sse")
@CrossOrigin("*")
public class SSEController {

    @Autowired
    private SseEmitterService sseEmitterService;

    @GetMapping("/createConnect")
    public SseEmitter createConnect(String clientId) {
        return sseEmitterService.createConnect(clientId);
    }

    @PostMapping("/broadcast")
    public void sendMessageToAllClient(@RequestBody(required = false) String msg) {
        sseEmitterService.sendMessageToAllClient(msg);
    }

    @PostMapping("/sendMessage")
    public void sendMessageToOneClient(@RequestBody(required = false) SseEmitterData sseEmitterData) {
        if (sseEmitterData.getClientId().isEmpty()) {
            return;
        }
        sseEmitterService.sendMessageToOneClient(sseEmitterData.getClientId(), sseEmitterData.getData().toString());
    }

    @GetMapping("/closeConnect")
    public void closeConnect(String clientId) {
        sseEmitterService.closeConnect(clientId);
    }

    @PostMapping("/test")
    public void test() throws IOException {
        sseEmitterService.test();
    }



    private final List<SseEmitter> list = new CopyOnWriteArrayList<>();

    @RequestMapping("subscribe")
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter();
        list.add(sseEmitter);
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion:{}", sseEmitter);
            list.remove(sseEmitter);
        });
        sseEmitter.onTimeout(() -> {
            log.info("onTimeout:{}", sseEmitter);
            list.remove(sseEmitter);
        });
        return sseEmitter;
    }


//    @PostConstruct
    public void init() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, r -> new Thread(()->{
        },"hh"));
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            String id = RandomUtil.randomString(32);
            SseEmitter.SseEventBuilder test = SseEmitter.event()
                    .id(id)
                    .name("test")
                    .data("test11111", MediaType.TEXT_PLAIN);
            Iterator<SseEmitter> iterator = list.iterator();
            while (iterator.hasNext()) {
                SseEmitter next = iterator.next();
                try {
                    next.send(test);
                } catch (IOException e) {
                    log.info(e.getMessage());
                }
            }
        }, 1, 2, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.shutdown();
    }

    @PostMapping("broadcast2")
    public String broadcast(@RequestBody SseEmitterData data) {
        String id = RandomUtil.randomString(32);
        SseEmitter.SseEventBuilder test = SseEmitter.event()
                .id(id)
                .name(data.getName())
                .data(data.getData(), MediaType.APPLICATION_JSON);
        Iterator<SseEmitter> iterator = list.iterator();
        while (iterator.hasNext()) {
            SseEmitter next = iterator.next();
            try {
                next.send(test);
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }
        return id;
    }


}
