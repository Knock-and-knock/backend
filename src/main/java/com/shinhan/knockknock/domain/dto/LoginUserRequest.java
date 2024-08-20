package com.shinhan.knockknock.domain.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "loginType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IdLoginUserRequest.class, name = "NORMAL"),
        @JsonSubTypes.Type(value = SimpleLoginUserRequest.class, name = "SIMPLE")
})
public class LoginUserRequest {
    private String loginType;
}
