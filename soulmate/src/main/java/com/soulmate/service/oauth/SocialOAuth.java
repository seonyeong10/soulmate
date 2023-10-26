package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface SocialOAuth {
    /**
     * 각 SNS 로그인 페이지로 redirect 할 URL build
     * 사용자로부터 로그인 요청을 받아 SNS 로그인 서버 인증용 코드 요청
     */
    String getOAuthRedirectURL();

    /**
     * 접근 토큰(AccessToken)을 요청한다.
     */
    ResponseEntity<String> requestAccessToken(String code);

    /**
     * 토큰(JSON)을 DTO 로 바꾼다.
     */
    Object getOAuthToken(ResponseEntity<String> response) throws JsonProcessingException;

    /**
     * 사용자 정보(JSON) 을 DTO로 바꾼다.
     */
    Object getUserInfo(ResponseEntity<String> response) throws JsonProcessingException;
}
