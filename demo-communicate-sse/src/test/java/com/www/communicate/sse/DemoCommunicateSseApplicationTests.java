package com.www.communicate.sse;

import com.www.communicate.sse.model.SseEmitterData;
import com.www.communicate.sse.service.SseEmitterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoCommunicateSseApplicationTests {

    @Autowired
    private SseEmitterService sseEmitterService;


    @Test
    void contextLoads() {
       sseEmitterService.sendMessageToOneClient("ddz","sseEmitterService");
    }

}
