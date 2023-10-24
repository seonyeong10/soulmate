package com.soulmate.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soulmate.domain.Member;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
public class NaverUser implements OAuthUser {
    private String id;

    private String nickname;

    private String name;

    private String email;

    private String gender;

    private String age;

    private String birthday;

    @JsonProperty("profile_image")
    private String profileImage;

    private String birthyear;

    private String mobile;

    @Override
    public Member toMember() {
        return Member.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .mobile(mobile)
                .picture(profileImage)
                .platformType(PlatformType.NAVER)
                .role(Role.USER)
                .build();
    }
}
