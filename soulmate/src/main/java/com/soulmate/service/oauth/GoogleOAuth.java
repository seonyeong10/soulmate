package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.web.dto.oauth.GoogleOAuthToken;
import com.soulmate.web.dto.oauth.GoogleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth implements SocialOAuth {
    @Value("${spring.oauth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;
    @Value("${spring.oauth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${spring.oauth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${spring.oauth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${spring.oauth2.google.scope}")
    private String GOOGLE_SNS_ACCESS_SCOPE;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String getOAuthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_SNS_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        //파라미터를 형식에 맞춰 구성한다.
        String parameterString = params.entrySet().stream()
                        .map(p -> p.getKey() + "=" + p.getValue())
                        .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;

        log.info("redirectURL >> " + redirectURL);
        return redirectURL;
    }

    /**
     * AccessToken 요청하기
     * @param code
     * @return
     */
    public ResponseEntity<String> requestAccessToken(String code) {
        String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    /**
     * AccessToken 을 GoogleOAuthToken 객체로 반환하기
     * @param response
     * @return
     */
    public GoogleOAuthToken getOAuthToken(ResponseEntity<String> response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
    }

    /**
     * 사용자 정보 요청하기
     * @param oAuthToken
     * @return
     */
    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccessToken());

        //HttpEntity 를 생성해 헤더를 담아 restTemplate 로 구글과 통신한다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        return response;
    }

    /**
     * 사용자 정보를 GoogleUser 객체로 반환하기
     */
    public GoogleUser getGoogleUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), GoogleUser.class);
    }
}
