package com.example.democommunicatestreamable.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class McpSession {
    private String sessionId;
    private long lastAccessTime;
    private Queue<String> pendingMessages = new ConcurrentLinkedQueue<>();

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Queue<String> getPendingMessages() {
        return pendingMessages;
    }

    public void setPendingMessages(Queue<String> pendingMessages) {
        this.pendingMessages = pendingMessages;
    }

    public McpSession() {
    }

    public McpSession(String sessionId, long lastAccessTime, Queue<String> pendingMessages) {
        this.sessionId = sessionId;
        this.lastAccessTime = lastAccessTime;
        this.pendingMessages = pendingMessages;
    }
}
