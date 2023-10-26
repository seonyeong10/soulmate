package com.soulmate.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soulmate.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
public class KakaoUser implements OAuthUser {

    private String name;

    private String nickname;

    private String picture;

    private String email;

    @JsonProperty("age_range")
    private String ageRange;

    private String brithday;

    private String gender;

    @Override
    public Member toMember() {
        return null;
    }
}
