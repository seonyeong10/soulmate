package com.soulmate.service.oauth;

public interface SocialOAuth {
    /**
     * 각 SNS 로그인 페이지로 redirect 할 URL build
     * 사용자로부터 로그인 요청을 받아 SNS 로그인 서버 인증용 코드 요청
     */
    String getOAuthRedirectURL();
}
