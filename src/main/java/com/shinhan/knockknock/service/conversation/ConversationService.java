package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.*;
import com.shinhan.knockknock.domain.dto.user.ReadUserResponse;
import com.shinhan.knockknock.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.concurrent.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService {

    private final TextResponseService textResponseService;
    private final TextToSpeechService textToSpeechService;
    private final ConversationLogService conversationLogService;
    private final ConversationRoomService conversationRoomService;
    private final UserService userService;

    public ConversationResponse conversation(ConversationRequest request, long userNo) {
        // 방 존재 여부 검사
        conversationRoomService.readConversationRoomByConversationRoomNo(request.getConversationRoomNo());

        // User Id 가져오기
        ReadUserResponse user = userService.readUser(userNo);

        // Chatbot 답변 생성
        ChatbotResponse response;
        try {
            response = executeWithTimeout(() -> textResponseService.TextResponse(request, user));
        } catch (TimeoutException e) {
            log.warn("⚠️ TextResponse timed out for input={}, conversationRoomNo={}", request.getInput(), request.getConversationRoomNo());
            response = ChatbotResponse.builder()
                    .content("응답 시간이 초과되었습니다. 다시 시도해 주세요.")
                    .build();
        } catch (Exception e) {
            log.error("❌ Exception occurred while getting TextResponse", e);
            response = ChatbotResponse.builder()
                    .content("문제가 발생했습니다. 다시 시도해 주세요.")
                    .build();
        }

        // Chatbot 답변 검사
        ConversationLogRequest conversationLog = makeConversationLogRequest(request, response);

        // 대화 내역 저장
        conversationLogService.createConversationLog(conversationLog);
        conversationRoomService.updateConversationRoomEndAt(request.getConversationRoomNo());

        // 음성 데이터 생성
        byte[] audioData = textToSpeechService.convertTextToSpeech(conversationLog.getConversationLogResponse());

        // 오디오 데이터를 Base64로 인코딩
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        ConversationResponse.RedirectionResult redirectionResult = null;
        if (response.getRedirectionResult() != null){
            redirectionResult = ConversationResponse.RedirectionResult.builder()
                    .serviceName(response.getRedirectionResult().getServiceName())
                    .serviceNumber(response.getRedirectionResult().getServiceNumber())
                    .serviceUrl(response.getRedirectionResult().getServiceUrl())
                    .build();
        }

        ConversationResponse.ReservationResult reservationResult = null;
        if (response.getReservationResult() != null) {
            reservationResult = ConversationResponse.ReservationResult.builder()
                    .welfareNo(response.getReservationResult().getServiceTypeNumber())
                    .welfareBookStartDate(response.getReservationResult().getReservationDate())
                    .welfareBookEndDate(response.getReservationResult().getReservationDate())
                    .welfareBookUseTime(response.getReservationResult().getReservationTimeNumber())
                    .build();
        }

        return ConversationResponse.builder()
                .content(response.getContent())
                .audioData(audioBase64)
                .actionRequired(response.isActionRequired())
                .totalTokens(response.getTotalTokens())
                .redirectionResult(redirectionResult)
                .reservationResult(reservationResult)
                .build();
    }

    private ConversationLogRequest makeConversationLogRequest(ConversationRequest request, ChatbotResponse response) {
        ConversationLogRequest conversationLog;
        String content = response.getContent();
        if (content == null || content.isEmpty()) {
            log.warn("⚠️ Chatbot response is empty: content={}, totalTokens={}", response.getContent(), response.getTotalTokens());
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse("문제가 발생했습니다. 다시 한번 말해주세요.")
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        } else {
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse(response.getContent())
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        }
        return conversationLog;
    }

    private <T> T executeWithTimeout(Callable<T> task)
            throws TimeoutException, Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(task);

        try {
            return future.get(7, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);  // 작업 취소
            throw e;  // 타임아웃 예외를 다시 던짐
        } finally {
            executor.shutdown();
        }
    }

}
