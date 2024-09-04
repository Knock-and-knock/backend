package com.shinhan.knockknock.service.conversation;

import com.shinhan.knockknock.domain.dto.conversation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {
    public void calculateToken(ChatbotResponse response, ClassificationResponse classificationResult) {
        log.debug("🪙 Classification Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", classificationResult.getPromptTokens(), classificationResult.getCompletionTokens(), classificationResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + classificationResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + classificationResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + classificationResult.getTotalTokens());
    }

    public void calculateToken(ChatbotResponse response, RedirectionResponse redirectionResult) {
        log.debug("🪙 Redirection Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", redirectionResult.getPromptTokens(), redirectionResult.getCompletionTokens(), redirectionResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + redirectionResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + redirectionResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + redirectionResult.getTotalTokens());
    }

    public void calculateToken(ChatbotResponse response, ReservationResponse reservationResult) {
        log.debug("🪙 Reservation Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", reservationResult.getPromptTokens(), reservationResult.getCompletionTokens(), reservationResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + reservationResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + reservationResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + reservationResult.getTotalTokens());
    }

    public void calculateToken(ChatbotResponse response, ConsumptionResponse consumptionResult) {
        log.debug("🪙 Reservation Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", consumptionResult.getPromptTokens(), consumptionResult.getCompletionTokens(), consumptionResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + consumptionResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + consumptionResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + consumptionResult.getTotalTokens());
    }

    public void calculateToken(ChatbotResponse response, ConsumptionReportResponse consumptionReportResult) {
        log.debug("🪙 Reservation Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", consumptionReportResult.getPromptTokens(), consumptionReportResult.getCompletionTokens(), consumptionReportResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + consumptionReportResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + consumptionReportResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + consumptionReportResult.getTotalTokens());
    }
}
