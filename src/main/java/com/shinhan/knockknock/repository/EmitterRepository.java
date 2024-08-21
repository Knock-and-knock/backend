package com.shinhan.knockknock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
    데이터베이스와 상호작용하지 않고, 메모리에 있는 ConcurrentHashMap을 사용하여 SseEmitter 객체를 관리
    따라서 Repository 인터페이스를 구현하지 않는다.
 */

@Repository
@RequiredArgsConstructor
public class EmitterRepository {
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /*
     * 주어진 userNo와 이미터를 저장
     *
     * @param userNo - 사용자 번호
     * @param emitter - 이벤트 Emitter
     */
    public void save(Long userNo, SseEmitter emitter) {
        emitters.put(userNo, emitter);
    }

    /*
     * 주어진 usrNo의 Emitter를 제거
     *
     * @param userNo - 사용자 번호
     */
    public void deleteByUserNo(Long userNo) {
        emitters.remove(userNo);
    }

    /*
     * 주어진 usrNo의 Emitter를 가져옴.
     *
     * @param userNo - 사용자 번호
     * @return SseEmitter - 이벤트 Emitter.
     */
    public SseEmitter get(Long userNo) {
        return emitters.get(userNo);
    }
}