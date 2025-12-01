package com.example.democommunicatestreamable.controller;

import com.example.democommunicatestreamable.domain.McpSession;
import com.example.democommunicatestreamable.service.McpSessionStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/mcp")
public class McpController {

    private final McpSessionStore store;

    public McpController(McpSessionStore store) {
        this.store = store; // ✅ Spring 会自动注入
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* ========== 1️⃣ 会话初始化（可选）========= */
    @PostMapping("/init")
    public Map<String, Object> initSession() {
        McpSession session = store.create();
        return Map.of(
                "sessionId", session.getSessionId(),
                "createdAt", System.currentTimeMillis()
        );
    }

    /* ========== 2️⃣ 客户端 POST 普通通信（带 sessionId）========= */
    @PostMapping
    public Map<String, Object> postMessage(@RequestBody Map<String, Object> body) {

        String sessionId = (String) body.get("sessionId");
        String content = (String) body.get("content");

        McpSession session = store.get(sessionId);
        if (session == null) {
            return Map.of("error", "invalid session");
        }

        // 模拟服务端处理
        String reply = "ACK: " + content;
        session.getPendingMessages().add(reply);

        return Map.of(
                "ok", true,
                "reply", reply
        );
    }

    /* ========== 3️⃣ Streamable HTTP 流式响应（POST + Chunked）========= */
    @PostMapping(value = "/stream", produces = "application/json;charset=UTF-8")
    public void streamResponse(@RequestBody Map<String, Object> body,
                               HttpServletResponse response) throws IOException {

        String sessionId = (String) body.get("sessionId");
        String prompt = (String) body.get("content");

        McpSession session = store.get(sessionId);

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter writer = response.getWriter();

        for (int i = 1; i <= 10; i++) {
            Map<String, Object> chunk = Map.of(
                    "sessionId", sessionId,
                    "delta", prompt + "-token-" + i,
                    "done", false
            );

            writer.write(objectMapper.writeValueAsString(chunk));
            writer.write("\n");
            writer.flush();

            sleep(300);
        }

        writer.write("{\"done\":true}\n");
        writer.flush();
    }

    /* ========== 4️⃣ 主动建立 SSE 长连接（服务器主动推送）========= */
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter openSse(@RequestParam String sessionId) {

        McpSession session = store.get(sessionId);
        SseEmitter emitter = new SseEmitter(0L);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String msg = session.getPendingMessages().poll();
                    if (msg != null) {
                        emitter.send(msg);
                    }
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                emitter.complete();
            }
        });

        return emitter;
    }

    /* ========== 5️⃣ 连接恢复（带 sessionId 拉取历史/未读消息）========= */
    @GetMapping("/resume")
    public Map<String, Object> resume(@RequestParam String sessionId) {

        McpSession session = store.get(sessionId);
        if (session == null) {
            return Map.of("error", "session not exist");
        }

        List<String> cached = new ArrayList<>();
        while (!session.getPendingMessages().isEmpty()) {
            cached.add(session.getPendingMessages().poll());
        }

        return Map.of(
                "sessionId", sessionId,
                "recoveredMessages", cached
        );
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}
