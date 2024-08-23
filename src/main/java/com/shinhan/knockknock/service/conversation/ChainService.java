package com.shinhan.knockknock.service.conversation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinhan.knockknock.domain.dto.conversationroom.ChatbotResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ClassificationResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.RedirectionResponse;
import com.shinhan.knockknock.domain.dto.conversationroom.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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

        return response;
    }
}
