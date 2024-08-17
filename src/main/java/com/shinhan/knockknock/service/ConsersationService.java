package com.shinhan.knockknock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.exception.ChatbotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


@Service
public class ConsersationService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${MODEL_NAME}")
    private String modelName;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public void consersation(ConversationRequest request) {
        ChatbotResponse response = chatbot(request.getInput());
        System.out.println(response);
    }

    private ChatbotResponse chatbot(String input) {
        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 요청 본문 데이터 생성
        Map<String, Object> requestBody = createChatbotRequest(input);

        // 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(API_URL).toUriString(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 응답 상태 코드 확인 및 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                return parseResponse(responseEntity.getBody());
            } catch (Exception e) {
                throw new ChatbotException("Failed to parse chatbot response", e);
            }
        }

        return ChatbotResponse.builder()
                .content("Error: " + responseEntity.getStatusCode())
                .build();
    }

    private Map<String, Object> createChatbotRequest(String input) {
        // 요청 본문 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);

        // 메시지 리스트 구성
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");

        Map<String, String> userMessage1 = new HashMap<>();
        userMessage1.put("role", "user");
        userMessage1.put("content", input);

        requestBody.put("messages", new Map[]{systemMessage, userMessage1});

        return requestBody;
    }

    public ChatbotResponse parseResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // 필요한 필드 추출
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();
        int promptTokens = rootNode.path("usage").path("prompt_tokens").asInt();
        int completionTokens = rootNode.path("usage").path("completion_tokens").asInt();
        int totalTokens = rootNode.path("usage").path("total_tokens").asInt();

        // DTO로 변환
        return new ChatbotResponse(content, promptTokens, completionTokens, totalTokens);
    }
}
