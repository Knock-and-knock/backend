package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.service.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notification")
@Tag(name = "알림", description = "알림 API")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    // 알림 구독
    @Operation(summary = "알림 구독", description= "구독 메서드를 수행해야 알림 받을 수 있어요")
    @GetMapping(value = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long id) { // emitter 연결 응답 객체를 반환
        return notificationService.subscribe(id); // 이벤트 객체 생성: [userNo=1] 응답
    }

    // 알림 전송 테스트 메서드
    @Operation(summary = "알림 전송 테스트 메서드", description= "알림 전송 테스트 메서드")
    @PostMapping("/send-data/1")
    public String sendDataTest() {
        notificationService.notify(1L, "알림");
        return "notification success";
    }
    // 나중엔 서버안에서 고정값을 동적으로 변경하여 알림을 사용할 때 userNo와 메시지를 추가해서 사용하도록
}