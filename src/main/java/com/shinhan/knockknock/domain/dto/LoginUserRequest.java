package com.shinhan.knockknock.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "protector01")
    private String userId;
    @Schema(example = "1234")
    private String userPassword;
    @Schema(example = "NORMAL")
    private String loginType;
    @Schema(example = "string")
    private String userSimplePassword;
    private String loginType;
}
