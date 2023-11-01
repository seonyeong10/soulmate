package com.soulmate.config.auth.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.domain.Member;
import com.soulmate.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;

    private String nameAttributeKey;

    private String name;

    private String email;

    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }

        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        response.put("id", attributes.get("id")); //IllegalArgumentException: Missing attribute 'id' in attributes

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("profile_image"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //OAuthAttributes 에서 엔티티를 생성하는 시점은 처음 가입할 때
    //최초 권한은 USER
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
