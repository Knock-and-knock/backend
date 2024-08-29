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

import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardIssueRequest {
    //private String cardIssueResidentNo;
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

    // AgreeDeserializer 클래스 내부에 추가
    public static class AgreeDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            // JSON 객체에서 status 필드를 읽어 boolean 값으로 변환
            JsonNode node = jp.getCodec().readTree(jp); // TreeNode를 JsonNode로 캐스팅
            return "true".equalsIgnoreCase(node.get("cardIssueIsAgree").asText());
        }
    }
}
