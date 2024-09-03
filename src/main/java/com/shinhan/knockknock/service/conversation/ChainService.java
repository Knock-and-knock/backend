package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainService {

    final ChatbotService chatbotService;

    /**
     * <pre>
     * 메소드명   : classificationChain
     * 설명       : 주어진 프롬프트에 따라 메인 작업 번호와 서브 작업 번호를 분류하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return ClassificationResponse 메인 작업 번호 및 서브 작업 번호를 포함하는 응답 객체
     * @throws JsonProcessingException JSON 처리 중 오류 발생 시
     */
    public ClassificationResponse classificationChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("mainTaskNumber", Map.of("type", "string"));
        responseSchema.put("subTaskNumber", Map.of("type", "string", "nullable", true));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());

        return ClassificationResponse.builder()
                .mainTaskNumber(rootNode.path("mainTaskNumber").asText().trim())
                .subTaskNumber(rootNode.path("subTaskNumber").asText().trim())
                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    /**
     * <pre>
     * 메소드명   : redirectionChain
     * 설명       : 주어진 프롬프트에 따라 서비스 리다이렉션 정보를 반환하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return RedirectionResponse 서비스 리다이렉션 정보를 포함하는 응답 객체
     * @throws JsonProcessingException JSON 처리 중 오류 발생 시
     */
    public RedirectionResponse redirectionChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("actionRequired", Map.of("type", "boolean"));
        responseSchema.put("serviceNumber", Map.of("type", "string"));
        responseSchema.put("serviceName", Map.of("type", "string"));
        responseSchema.put("serviceUrl", Map.of("type", "string"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        return RedirectionResponse.builder()
                .actionRequired(rootNode.path("actionRequired").asBoolean())
                .serviceNumber(rootNode.path("serviceNumber").asText().trim())
                .serviceName(rootNode.path("serviceName").asText().trim())
                .serviceUrl(rootNode.path("serviceUrl").asText().trim())
                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    /**
     * <pre>
     * 메소드명   : reservationChain
     * 설명       : 주어진 프롬프트에 따라 예약 정보를 처리하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return ReservationResponse 예약 정보를 포함하는 응답 객체
     * @throws JsonProcessingException JSON 처리 중 오류 발생 시
     */
    public ReservationResponse reservationChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("actionRequired", Map.of("type", "boolean"));
        responseSchema.put("serviceTypeNumber", Map.of("type", "number"));
        responseSchema.put("reservationDate", Map.of("type", "string"));
        responseSchema.put("reservationTimeNumber", Map.of("type", "string"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        return ReservationResponse.builder()
                .actionRequired(rootNode.path("actionRequired").asBoolean())
                .serviceTypeNumber(rootNode.path("serviceTypeNumber").asInt())
                .reservationDate(rootNode.path("reservationDate").asText())
                .reservationTimeNumber(rootNode.path("reservationTimeNumber").asInt())
                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    public ConsumptionResponse consumptionChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("startYear", Map.of("type", "number"));
        responseSchema.put("startMonth", Map.of("type", "number"));
        responseSchema.put("startDay", Map.of("type", "number"));
        responseSchema.put("endYear", Map.of("type", "number"));
        responseSchema.put("endMonth", Map.of("type", "number"));
        responseSchema.put("endDay", Map.of("type", "number"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        return ConsumptionResponse.builder()
                .startYear(rootNode.path("startYear").asInt())
                .startMonth(rootNode.path("startMonth").asInt())
                .startDay(rootNode.path("startDay").asInt())
                .endYear(rootNode.path("endYear").asInt())
                .endMonth(rootNode.path("endMonth").asInt())
                .endDay(rootNode.path("endDay").asInt())

                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    public ConsumptionReportResponse consumptionReportChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("year", Map.of("type", "number"));
        responseSchema.put("month", Map.of("type", "number"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        return ConsumptionReportResponse.builder()
                .year(rootNode.path("year").asInt())
                .month(rootNode.path("month").asInt())
                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    /**
     * <pre>
     * 메소드명   : chatbotChain
     * 설명       : 응답 스키마 없이 일반적인 챗봇 응답을 처리하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return ChatbotResponse 챗봇 응답 객체
     */
    public ChatbotResponse chatbotChain(List<Map<String, String>> prompt) {
        return chatbotService.getChatbotResponse(prompt);
    }
}
