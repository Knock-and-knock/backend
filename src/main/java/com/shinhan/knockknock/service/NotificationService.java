package com.shinhan.knockknock.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(Long userNo);
    void notify(Long userNo, Object event);
    void sendToClient(Long userNo, Object data);
    SseEmitter createEmitter(Long userNo);
}
