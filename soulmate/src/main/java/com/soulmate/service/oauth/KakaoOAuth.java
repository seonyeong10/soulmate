package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.web.dto.oauth.KakaoOAuthToken;
import com.soulmate.web.dto.oauth.KakaoUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuth implements SocialOAuth {
    @Value("${spring.oauth2.kakao.url}")
    private String KAKAO_SNS_LOGIN_URL;

    @Value("${spring.oauth2.kakao.client-id}")
    private String KAKAO_SNS_CLIENT_ID;

    @Value("${spring.oauth2.kakao.client-secret}")
    private String KAKAO_SNS_CLIENT_SECRET;

    @Value("${spring.oauth2.kakao.callback-url}")
    private String KAKAO_SNS_CALLBACK_URL;

    @Value("${spring.oauth2.kakao.scope}")
    private String KAKAO_SNS_SCOPE;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public String getOAuthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.put("response_type", "code");

        String parameterString = params.entrySet().stream()
                .map(p -> p.getKey() + "=" + p.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = KAKAO_SNS_LOGIN_URL + "?" + parameterString;

        log.info("redirectURL >> " + redirectURL);
        return redirectURL;
    }

    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        //POST 요청
        String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_SNS_CLIENT_ID);
        params.add("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.add("code", code);
        params.add("client_secret", KAKAO_SNS_CLIENT_SECRET);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_TOKEN_REQUEST_URL, HttpMethod.POST, request, String.class);

        return response;
    }

    @Override
    public KakaoOAuthToken getOAuthToken(ResponseEntity<String> response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), KakaoOAuthToken.class);
    }

    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken) {
        String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        return response;
    }

    @Override
    public KakaoUser getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        JsonNode kakaoUserNode = objectMapper.readTree(response.getBody());

        KakaoUser kakaoUser = objectMapper.readValue(kakaoUserNode.get("kakao_account").toString(), KakaoUser.class);
        //닉네임
        kakaoUser.setNickname(kakaoUserNode.get("kakao_account").get("profile").get("nickname").textValue());

        return kakaoUser;
    }
}
