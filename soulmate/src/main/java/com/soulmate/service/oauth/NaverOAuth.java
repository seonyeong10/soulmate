package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.web.dto.oauth.NaverOAuthToken;
import com.soulmate.web.dto.oauth.NaverUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
public class NaverOAuth implements SocialOAuth {
    @Value("${spring.oauth2.naver.url}")
    private String NAVER_SNS_LOGIN_URL;

    @Value("${spring.oauth2.naver.client-id}")
    private String NAVER_SNS_CLIENT_ID;

    @Value("${spring.oauth2.naver.client-secret}")
    private String NAVER_SNS_CLIENT_SECRET;

    @Value("${spring.oauth2.naver.callback-url}")
    private String NAVER_SNS_CALLBACK_URL;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public String getOAuthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", NAVER_SNS_CLIENT_ID);
        params.put("redirect_uri", NAVER_SNS_CALLBACK_URL);
        params.put("state", "state");

        //파라미터를 형식에 맞춰 구성하기
        String parameterString = params.entrySet().stream()
                .map(p -> p.getKey() + "=" + p.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = NAVER_SNS_LOGIN_URL + "?" + parameterString;

        log.info("redirectURL >> " + redirectURL);
        return redirectURL;
    }

    /**
     * callback 으로 받은 code 로 접근 토큰 발급받기
     * @param code callback url 로 받은 code
     * @return 접큰 토큰
     */
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        String NAVER_TOKEN_REQUEST_URL = "https://nid.naver.com/oauth2.0/token";

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", NAVER_SNS_CLIENT_ID);
        params.put("client_secret", NAVER_SNS_CLIENT_SECRET);
        params.put("state", "state");
        params.put("grant_type", "authorization_code");

        //post 전송하면 grant_type is missing err
        //ResponseEntity<String> responseEntity = restTemplate.postForEntity(NAVER_TOKEN_REQUEST_URL, params, String.class);
        String parameterString = params.entrySet().stream()
                .map(p -> p.getKey() + "=" + p.getValue())
                .collect(Collectors.joining("&"));

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(NAVER_TOKEN_REQUEST_URL + "?" + parameterString, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }

    /**
     * JSON 형식의 접근 토큰을 NaverOAuthToken객체로 변환하기
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public NaverOAuthToken getOAuthToken(ResponseEntity<String> response) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), NaverOAuthToken.class);
    }

    /**
     * 접근 토큰으로 사용자 정보 요청하기
     * @param naverOAuthToken 발급받은 토큰 객체
     * @return
     */
    public ResponseEntity<String> requestUserInfo(NaverOAuthToken naverOAuthToken) {
        String NAVER_USERINFO_REQUEST_URL = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverOAuthToken.getAccessToken());

        //HttpEntity 를 생성해 헤더를 담아 restTemplate 로 네이버와 통신하기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(NAVER_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);

        return response;
    }

    /**
     * 사용자 정보를 NaverUser 객체로 변환하기
     * @param response
     * @return
     */
    @Override
    public NaverUser getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        JsonNode naverUserNode = objectMapper.readTree(response.getBody());
        String message = naverUserNode.get("message").textValue();

        if (message.equals("success")) {
            return objectMapper.readValue(naverUserNode.get("response").toString(), NaverUser.class);
        }
        return null;
    }
}
