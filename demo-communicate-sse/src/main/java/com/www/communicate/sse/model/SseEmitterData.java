package com.www.communicate.sse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SseEmitterData {
    private String clientId;
    private String name;
    private Object data;
}
