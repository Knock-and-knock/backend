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
