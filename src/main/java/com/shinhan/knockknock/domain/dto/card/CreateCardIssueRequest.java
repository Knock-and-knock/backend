package com.shinhan.knockknock.domain.dto.card;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shinhan.knockknock.domain.entity.RiskEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardIssueRequest {

    private static final Logger logger = LoggerFactory.getLogger(CreateCardIssueRequest.class);

    private String cardIssueKname;
    private String cardIssuePhone;
    private String cardIssueFirstEname;
    private String cardIssueLastEname;
    private String cardIssueEname;
    private String cardIssueEmail;
    private String cardIssueBank;
    private String cardIssueAccount;

    @JsonDeserialize(using = AgreeDeserializer.class)
    private boolean cardIssueIsAgree;

    private String cardIssueIncome;
    private String cardIssueCredit;
    private String cardIssueAmountDate;
    private String cardIssueSource;
    private RiskEnum cardIssueIsHighrisk;
    private String cardIssuePurpose;
    private String cardIssueAddress;
    private boolean cardIssueIsFamily;
    private String cardIssuePassword;
    private String cardIssueFirstAddress;
    private String cardIssueSecondAddress;

    public void validate() {

        if (cardIssuePassword == null || cardIssuePassword.isEmpty()) {
            logger.error("카드 발급 요청의 비밀번호가 유효하지 않습니다: {}", cardIssuePassword);
            throw new IllegalArgumentException("카드 발급 비밀번호는 필수입니다.");
        }

        if (cardIssueBank == null || cardIssueBank.isEmpty()) {
            logger.error("카드 발급 요청의 은행명이 유효하지 않습니다: {}", cardIssueBank);
            throw new IllegalArgumentException("카드 발급 요청의 은행명은 필수입니다.");
        }

        if (cardIssueAccount == null || cardIssueAccount.isEmpty()) {
            logger.error("카드 발급 요청의 계좌 번호가 유효하지 않습니다: {}", cardIssueAccount);
            throw new IllegalArgumentException("카드 발급 요청의 계좌 번호는 필수입니다.");
        }

        if (cardIssueIncome == null || cardIssueIncome.isEmpty()) {
            logger.error("카드 발급 요청의 소득 정보가 유효하지 않습니다: {}", cardIssueIncome);
            throw new IllegalArgumentException("카드 발급 요청의 소득 정보는 필수입니다.");
        }

        if (cardIssueCredit == null || cardIssueCredit.isEmpty()) {
            logger.error("카드 발급 요청의 신용 정보가 유효하지 않습니다: {}", cardIssueCredit);
            throw new IllegalArgumentException("카드 발급 요청의 신용 정보는 필수입니다.");
        }

        if (cardIssueAmountDate == null || cardIssueAmountDate.isEmpty()) {
            logger.error("카드 발급 요청의 결제 날짜가 유효하지 않습니다: {}", cardIssueAmountDate);
            throw new IllegalArgumentException("카드 발급 요청의 금액 날짜는 필수입니다.");
        }

        if (cardIssueSource == null || cardIssueSource.isEmpty()) {
            logger.error("카드 발급 요청의 자금 출처가 유효하지 않습니다: {}", cardIssueSource);
            throw new IllegalArgumentException("카드 발급 요청의 자금 출처는 필수입니다.");
        }

        if (cardIssuePurpose == null || cardIssuePurpose.isEmpty()) {
            logger.error("카드 발급 요청의 목적이 유효하지 않습니다: {}", cardIssuePurpose);
            throw new IllegalArgumentException("카드 발급 요청의 목적은 필수입니다.");
        }
    }

    public static class AgreeDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);

            // 중첩된 구조 처리
            if (node.has("cardIssueIsAgree")) {
                JsonNode nestedNode = node.get("cardIssueIsAgree");
                if (nestedNode.isBoolean()) {
                    return nestedNode.asBoolean();  // 중첩된 Boolean 값 처리
                } else if (nestedNode.isTextual()) {
                    return "true".equalsIgnoreCase(nestedNode.asText());  // 중첩된 문자열 처리
                }
            }

            // 단순한 구조 처리
            if (node.isBoolean()) {
                return node.asBoolean();  // Boolean 값 처리
            } else if (node.isTextual()) {
                String textValue = node.asText();
                if ("true".equalsIgnoreCase(textValue)) {
                    return true;
                } else if ("false".equalsIgnoreCase(textValue)) {
                    return false;
                }
            }

            throw new IOException("Invalid value for cardIssueIsAgree: " + node.asText());
        }
    }
}
