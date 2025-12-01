package com.example.democommunicatestreamable.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StreamHttpController {

    @PostMapping(value = "/stream/chat", produces = "application/json;charset=UTF-8")
    public void streamChat(@RequestBody Map<String, String> body,
                           HttpServletResponse response) throws IOException {

        // ✅ 核心流式响应头
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        PrintWriter writer = response.getWriter();

        String prompt = body.get("prompt");

        // ✅ 模拟 AI token 流式输出
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("index", i);
            obj.put("delta", "Token-" + i);
            obj.put("done", false);

            writer.write(new ObjectMapper().writeValueAsString(obj));
            writer.write("\n");      // ✅ 行分隔（前端靠这个拆包）
            writer.flush();           // ✅ 关键：立即推送

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                break;
            }
        }

        // ✅ 结束标识
        writer.write("{\"done\":true}\n");
        writer.flush();
    }
}
