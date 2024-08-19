package com.shinhan.knockknock.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.exception.ChatbotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${MODEL_NAME}")
    private String modelName;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * <pre>
     * 메소드명 : chatbot
     * 설명     : 주어진 입력을 바탕으로 OpenAI의 Chatbot API를 호출하여 응답을 받아온다.
     * </pre>
     * @param input 사용자가 입력한 메시지
     * @return ChatbotResponse API로부터 반환된 응답 데이터 객체
     * @throws ChatbotException API 호출 또는 응답 처리 중 예외 발생 시 던짐
     */
    public ChatbotResponse chatbot(String input) {
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

    /**
     * <pre>
     * 메소드명 : createChatbotRequest
     * 설명     : Chatbot API에 요청할 본문 데이터를 생성한다.
     * </pre>
     * @param input 사용자가 입력한 메시지
     * @return Map<String, Object> 생성된 요청 본문 데이터
     */
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

    /**
     * <pre>
     * 메소드명 : parseResponse
     * 설명     : Chatbot API로부터 받은 JSON 응답을 파싱하여 ChatbotResponse 객체로 변환한다.
     * </pre>
     * @param jsonResponse API로부터 받은 JSON 응답
     * @return ChatbotResponse 파싱된 응답 데이터를 담은 객체
     * @throws Exception JSON 파싱 중 오류 발생 시 던짐
     */
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
