package com.soulmate.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GoogleOAuthToken extends OAuthToken {
//    @JsonProperty("access_token")
//    private String accessToken;

//    @JsonProperty("expires_in")
//    private String expiresIn;

    private String scope;

//    @JsonProperty("token_type")
//    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;
}
