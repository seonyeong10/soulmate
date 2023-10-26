package com.soulmate.web.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OAuthCallbackReqDto {
    private String code; //callback parameter

    private String state;

    private String error;

    @JsonAlias({"error_description", "errorDesc"})
    private String errorDescription;

    public String sendErrorParam() {
        return "?error=" + error + "&desc=" + errorDescription;
    }
}
