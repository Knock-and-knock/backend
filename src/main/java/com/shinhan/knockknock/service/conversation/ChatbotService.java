package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ClassificationResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.InstructionResponse;
import com.shinhan.knockknock.exception.ChatbotException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatbotService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${MODEL_NAME}")
    private String modelName;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ClassificationResponse classificationChain(List<Map<String, String>> classificationPrompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("mainTaskNumber", Map.of("type", "string"));
        responseSchema.put("subTaskNumber", Map.of("type", "string", "nullable", true));

        ChatbotResponse response = getChatbotResponse(classificationPrompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());

        return ClassificationResponse.builder()
                .mainTaskNumber(rootNode.path("mainTaskNumber").asText().trim())
                .subTaskNumber(rootNode.path("subTaskNumber").asText().trim())
                .build();
    }

    public InstructionResponse instructionChain(List<Map<String, String>> instructionPrompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("actionRequired", Map.of("type", "boolean"));
        responseSchema.put("serviceNumber", Map.of("type", "string"));

        ChatbotResponse response = getChatbotResponse(instructionPrompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        return InstructionResponse.builder()
                .actionRequired(rootNode.path("actionRequired").asText().trim())
                .serviceNumber(rootNode.path("serviceNumber").asText().trim())
                .build();
    }

    public ChatbotResponse chatbotChain(List<Map<String, String>> chatbotPrompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("content", Map.of("type", "string"));

        ChatbotResponse response = getChatbotResponse(chatbotPrompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());

        response.setContent(rootNode.path("content").asText());

        return response;
    }

    private ChatbotResponse getChatbotResponse(List<Map<String, String>> messagesList, Map<String, Object> responseSchema) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 요청 본문 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);

        // messagesList를 배열로 변환하여 requestBody에 추가
        requestBody.put("messages", messagesList.toArray(new Map[0]));

        // Response Format 생성
        Map<String, Object> jsonSchema = createJsonSchema(responseSchema);
        requestBody.put("response_format", jsonSchema);

        // HTTP Entity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        printChatbotRequest(requestBody);

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

    private Map<String, Object> createJsonSchema(Map<String, Object> properties) {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("strict", true);

        schema.put("properties", properties);

        // properties 맵의 모든 키를 필수 필드로 설정
        List<String> requiredFields = new ArrayList<>(properties.keySet());
        schema.put("required", requiredFields);

        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", "TaskNumberSchema");  // 스키마 이름 추가
        jsonSchema.put("schema", schema);  // 여기에 추가

        Map<String, Object> responseFormat = new HashMap<>();
        responseFormat.put("type", "json_schema");
        responseFormat.put("json_schema", jsonSchema);

        return responseFormat;
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
        return ChatbotResponse.builder()
                .content(content)
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .totalTokens(totalTokens)
                .build();
    }

    private void printChatbotRequest(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonRequestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            log.debug("Final JSON Request Body:\n{}", jsonRequestBody);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert request body to JSON: {}", e.getMessage());
        }
    }
}
