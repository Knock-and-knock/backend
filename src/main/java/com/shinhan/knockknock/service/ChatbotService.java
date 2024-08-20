package com.shinhan.knockknock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationLogResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ConversationRequest;
import com.shinhan.knockknock.exception.ChatbotException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${MODEL_NAME}")
    private String modelName;

    @Value("classpath:prompts/chatbot_chain_system.prompt")
    private Resource systemPromptResource;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    //    public ChatbotResponse chatbot(ConversationRequest request, List<ConversationLogResponse> conversationLogs) {
//        // RestTemplate 객체 생성
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 요청 생성
//        HttpEntity<Map<String, Object>> requestEntity = createChatbotRequest(request.getInput(), conversationLogs);
//
//        // API 호출 및 응답 받기
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                UriComponentsBuilder.fromHttpUrl(API_URL).toUriString(),
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        // 응답 상태 코드 확인 및 처리
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            try {
//                return parseResponse(responseEntity.getBody());
//            } catch (Exception e) {
//                throw new ChatbotException("Failed to parse chatbot response", e);
//            }
//        }
//
//        return ChatbotResponse.builder()
//                .content("Error: " + responseEntity.getStatusCode())
//                .build();
//    }
    public void classificationChain(String input) {

    }

    public ChatbotResponse chatbotChain(ConversationRequest request, List<ConversationLogResponse> conversationLogs) {
        String systemPrompt = loadSystemPrompt(systemPromptResource);
        List<Map<String, String>> messagesList = createMessagesList(systemPrompt, request.getInput(), conversationLogs);

        return getChatbotResponse(messagesList);
    }

    private ChatbotResponse getChatbotResponse(List<Map<String, String>> messagesList) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 요청 본문 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);

        // messagesList를 배열로 변환하여 requestBody에 추가
        requestBody.put("messages", messagesList.toArray(new Map[0]));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // JSON 변환 및 출력
        //printChatbotRequest(requestBody);

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

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
     * 메소드명   : createMessagesList
     * 설명       : 사용자의 입력과 대화 로그를 기반으로 챗봇 API에 전달할 메시지 리스트를 생성합니다.
     * </pre>
     *
     * @param input            사용자가 입력한 메시지
     * @param conversationLogs 이전 대화 로그 리스트
     * @return List<Map < String, String>>  생성된 메시지 리스트
     */
    private List<Map<String, String>> createMessagesList(String systemPrompt, String input, List<ConversationLogResponse> conversationLogs) {
        List<Map<String, String>> messagesList = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messagesList.add(systemMessage);

        // 대화 로그 추가
        for (ConversationLogResponse log : conversationLogs) {
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", log.getConversationLogInput());
            messagesList.add(userMessage);

            Map<String, String> assistantMessage = new HashMap<>();
            assistantMessage.put("role", "assistant");
            assistantMessage.put("content", log.getConversationLogResponse());
            messagesList.add(assistantMessage);
        }

        // 사용자 입력 메시지 추가
        Map<String, String> userMessage1 = new HashMap<>();
        userMessage1.put("role", "user");
        userMessage1.put("content", input);
        messagesList.add(userMessage1);

        return messagesList;
    }

    private String loadSystemPrompt(Resource systemPromptResource) {
        try {
            Path path = Paths.get(systemPromptResource.getURI());
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ChatbotException("Failed to load system prompt", e);
        }
    }

    /**
     * <pre>
     * 메소드명   : parseResponse
     * 설명       : Chatbot API로부터 받은 JSON 응답을 파싱하여 ChatbotResponse 객체로 변환합니다.
     * </pre>
     *
     * @param jsonResponse API로부터 받은 JSON 응답
     * @return ChatbotResponse   파싱된 응답 데이터를 담은 객체
     * @throws Exception JSON 파싱 중 오류 발생 시 던짐
     */
    private ChatbotResponse parseResponse(String jsonResponse) throws Exception {
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

    private void printChatbotRequest(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonRequestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            System.out.println("Final JSON Request Body:\n" + jsonRequestBody);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to convert request body to JSON: " + e.getMessage());
        }
    }
}
