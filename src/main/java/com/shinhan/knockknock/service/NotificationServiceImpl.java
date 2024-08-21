package com.shinhan.knockknock.service;

import com.shinhan.knockknock.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param userNo - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter subscribe(Long userNo) {
        SseEmitter emitter = createEmitter(userNo);
        sendToClient(userNo, "이벤트 객체 생성: [userNo=" + userNo + "]");
        return emitter;
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     *
     * @param userNo - 메세지를 전송할 사용자의 아이디.
     * @param event  - 전송할 이벤트 객체.
     */
    public void notify(Long userNo, Object event) {
        sendToClient(userNo, event);
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param userNo - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    public void sendToClient(Long userNo, Object data) {
        SseEmitter emitter = emitterRepository.get(userNo);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(userNo)).name("userNo1").data(data));
            } catch (IOException exception) {
                emitterRepository.deleteByUserNo(userNo);
                throw new RuntimeException("연결 오류!");
            }
        }
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param userNo - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    public SseEmitter createEmitter(Long userNo) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userNo, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteByUserNo(userNo));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteByUserNo(userNo));

        return emitter;
    }
}