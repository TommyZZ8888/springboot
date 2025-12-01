package com.example.democommunicatestreamable.service;

import com.example.democommunicatestreamable.domain.McpSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class McpSessionStore {

    private final Map<String, McpSession> sessions = new ConcurrentHashMap<>();

    public McpSession create() {
        String sid = UUID.randomUUID().toString();
        McpSession s = new McpSession(sid, System.currentTimeMillis(), new ConcurrentLinkedQueue<>());
        sessions.put(sid, s);
        return s;
    }

    public McpSession get(String sid) {
        return sessions.get(sid);
    }
}
