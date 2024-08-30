package com.shinhan.knockknock.controller;

import com.shinhan.knockknock.auth.JwtProvider;
import com.shinhan.knockknock.domain.dto.notification.ReadNotificationResponse;
import com.shinhan.knockknock.domain.entity.NotificationEntity;
import com.shinhan.knockknock.service.notification.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import retrofit2.http.PUT;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/notification")
@Tag(name = "7. 알림", description = "알림 API")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImpl notificationService;
    private final JwtProvider jwtProvider;

    // 알림 구독
    @Operation(summary = "알림 구독", description= "구독 메서드를 수행해야 알림 받을 수 있어요(항상 받을 준비해야해요)")
    @GetMapping(value = "/{userNo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userNo) {
        return notificationService.subscribe(userNo);
    }

    // 알림 구독 JWT 용
    @Operation(summary = "JWT 사용 알림 구독", description= "jwt 이용한 알림 구독 (swagger에서 알림 테스트 불가)")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeNotificationByJwt(@RequestHeader("Authorization") String header){
        return notificationService.subscribe(jwtProvider.getUserNoFromHeader(header));
    }

    // 알림 전송 테스트 메서드
    @Operation(summary = "알림 전송 테스트 메서드", description= "알림 전송 테스트 메서드")
    @PostMapping
    public String sendDataTest(@RequestBody Map<String, Long> payload) {
        Long userNo = payload.get("userNo");
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationCategory("알림")
                .notificationTitle("알림 전송 테스트")
                .notificationContent("알림 전송 테스트 성공")
                .userNo(userNo)
                .build();

        notificationService.notify(notificationEntity);
        return "notification success";
    }

    // 해당 사용자 알림 리스트 전체 조회
    @Operation(summary = "사용자 알림 전체 조회", description= "사용자 알림 전체 조회")
    @GetMapping("/read")
    public List<ReadNotificationResponse> readNotifications(@RequestHeader("Authorization") String header){
        return notificationService.readNotifications(jwtProvider.getUserNoFromHeader(header));
    }

    // 알림 상세 조회
    @Operation(summary = "사용자 알림 상세 조회", description="사용자 알림 상세 조회")
    @GetMapping("/read/{notificationNo}")
    public ReadNotificationResponse readNotification(@PathVariable("notificationNo") Long notificationNo){
        return notificationService.readNotification(notificationNo);
    }

    // 안 읽은 알림 개수 조회
    @Operation(summary = "읽지 않은 알림 개수 조회", description="읽지 않은 알림 개수 조회")
    @GetMapping("/read/count")
    public Long readNotificationCount(@RequestHeader("Authorization") String header){
        return notificationService.readNotificationCount(jwtProvider.getUserNoFromHeader(header));
    }

    // 알림 전체 읽기
    @Operation(summary = "알림 전체 체크", description= "알림 전체 체크")
    @PutMapping("/allcheck")
    public String readAllNotifications(@RequestHeader("Authorization") String header){
        Long userNo = jwtProvider.getUserNoFromHeader(header);
        notificationService.readAllNotifications(userNo);
        return "notification success";
    }

}