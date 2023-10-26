package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.soulmate.domain.Member;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.web.dto.oauth.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOAuth googleOAuth;

    private final NaverOAuth naverOAuth;

    private final KakaoOAuth kakaoOAuth;

    private final HttpServletResponse response;
    private final MemberRepository memberRepository;

    public void getRedirectURL(PlatformType platformType) throws IOException {
        String redirectURL;

        switch (platformType) {
            case GOOGLE -> {
                redirectURL = googleOAuth.getOAuthRedirectURL();
                break;
            }
            case NAVER -> {
                redirectURL = naverOAuth.getOAuthRedirectURL();
                break;
            }
            case KAKAO -> {
                redirectURL = kakaoOAuth.getOAuthRedirectURL();
                break;
            }
            default -> throw new IllegalArgumentException("알 수 없는 로그인 형식입니다.");
        }

        response.sendRedirect(redirectURL);
    }

    /**
     * 사용자 정보를 받아 로그인 하기
     */
    public UserResDto oAuthLogin(PlatformType platformType, String code) throws JsonProcessingException {
        UserResDto user;

        switch (platformType) {
            case GOOGLE -> {
                //일회성 코드를 보내 액세스 토큰이 담긴 객체 받기
                ResponseEntity<String> accessTokenResponse = googleOAuth.requestAccessToken(code);

                if (accessTokenResponse == null) {
                    throw new IllegalArgumentException("잘못된 로그인 입니다.");
                }

                //JSON 형식의 응답을 GoogleOAuthToken 객체에 담는다.
                GoogleOAuthToken googleOAuthToken = googleOAuth.getOAuthToken(accessTokenResponse);

                //액세스 토큰을 보내 사용자 정보를 받는다.
                ResponseEntity<String> userInfoResponse = googleOAuth.requestUserInfo(googleOAuthToken);

                //다시 JSON 형식의 응답을 GoogleUser에 담는다.
                GoogleUser googleUser = googleOAuth.getUserInfo(userInfoResponse);
                //log.info("googleUser >> " + googleUser.toString());

                //등록된 회원인지 확인하고, 신규 회원이면 저장한다.
                user = memberRepository.findByEmail(googleUser.getEmail()).stream()
                        .findAny().map(UserResDto::new)
                        .orElse(null);

                if (user == null) {
                    user = new UserResDto(memberRepository.save(googleUser.toMember()));
                }

                return user;
            }
            case NAVER -> {
                //액세스 토큰 발급받기
                ResponseEntity<String> accessTokenResponse = naverOAuth.requestAccessToken(code);
                log.info("naver accessToken >>> " + accessTokenResponse);

                if (accessTokenResponse == null) {
                    throw new IllegalArgumentException("잘못된 로그인 입니다.");
                }

                //JSON 형식의 접근 토큰을 NaverOAuthToken에 담기
                NaverOAuthToken naverOAuthToken = naverOAuth.getOAuthToken(accessTokenResponse);

                //접근 토큰으로 사용자 정보 받기
                ResponseEntity<String> userInfoResponse = naverOAuth.requestUserInfo(naverOAuthToken);
                log.info("naver userInfoResponse >>> " + userInfoResponse);

                //JSON 형식의 응답을 NaverUser 객체로 만들기
                NaverUser naverUser = naverOAuth.getUserInfo(userInfoResponse);
                
                //등록된 회원인지 확인하고, 신규 회원이면 저장하기
                user = memberRepository.findByEmail(naverUser.getEmail()).stream()
                        .findAny().map(UserResDto::new)
                        .orElse(null);

                if (user == null) {
                    user = new UserResDto(memberRepository.save(naverUser.toMember()));
                }

                return user;
            }
            case KAKAO -> {
                //액세스 토큰 받기
                ResponseEntity<String> accessTokenResponse = kakaoOAuth.requestAccessToken(code);
                log.info("kakao accessToken >>> " + accessTokenResponse);

                if (accessTokenResponse == null) {
                    throw new IllegalArgumentException("잘못된 로그인 입니다.");
                }

                //JSON 형식의 접근 토큰을 KakaoOAuthToken에 담기
                KakaoOAuthToken kakaoOAuthToken = kakaoOAuth.getOAuthToken(accessTokenResponse);

                //접근 토큰으로 사용자 정보 받기
                ResponseEntity<String> userInfoResponse = kakaoOAuth.requestUserInfo(kakaoOAuthToken);
                log.info("kakao userInfoResponse >>> " + userInfoResponse);

                //JSON 형식의 응답을 KakaoUser 객체에 담기
                KakaoUser kakaoUser = kakaoOAuth.getUserInfo(userInfoResponse);

                //등록된 회원인지 확인하고, 신규 회원이면 저장하기
                user = memberRepository.findByEmail(kakaoUser.getEmail()).stream()
                        .findAny().map(UserResDto::new)
                        .orElse(null);

                if (user == null) {
                    user = new UserResDto(memberRepository.save(kakaoUser.toMember()));
                }
                return user;
            }
            default -> throw new IllegalArgumentException("알 수 없는 로그인 형식입니다.");
        }
    }
}
