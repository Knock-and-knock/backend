package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversation.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversation.ClassificationResponse;
import com.shinhan.knockknock.domain.dto.conversation.RedirectionResponse;
import com.shinhan.knockknock.domain.dto.conversation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainService {

    final ChatbotService chatbotService;

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
                .build();
    }

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
                .build();
    }

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
                .build();
    }

    public ChatbotResponse chatbotChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("content", Map.of("type", "string"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());

        response.setContent(rootNode.path("content").asText());

        String content = extractContentUsingRegex(response.getContent());
        log.warn("&&&& {}", content);

        return response;
    }

    private String extractContentUsingRegex(String rawContent) {
        // "content":"와 그에 대응하는 문자열을 찾는 정규식
        Pattern pattern = Pattern.compile("\"content\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(rawContent);

        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 그룹은 "content":" 뒤에 나오는 실제 문자열입니다.
        }
        // content 부분을 제대로 찾지 못했을 경우 원본을 그대로 반환 (안전 장치)
        return rawContent;
    }
}
