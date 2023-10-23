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
public class GoogleUser implements OAuthUser {
    private String id;

    private String email;

    @JsonProperty("verified_email")
    private String verifiedEmail;

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;

    private String locale;

    @Override
    public Member toMember() {
        return Member.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .build();
    }
}
