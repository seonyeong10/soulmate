package com.soulmate.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
public class KakaoOAuthToken extends OAuthToken {
//    @JsonProperty("token_type")
//    private String tokenType;

//    @JsonProperty("access_token")
//    private String accessToken;

    @JsonProperty("id_token")
    private String idToken;

//    @JsonProperty("expires_in")
//    private String expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_token_expires_in")
    private String refreshTokenExpiresIn;
}
