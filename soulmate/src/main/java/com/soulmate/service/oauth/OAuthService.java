package com.soulmate.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soulmate.domain.Member;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.web.dto.oauth.GoogleOAuthToken;
import com.soulmate.web.dto.oauth.GoogleUser;
import com.soulmate.web.dto.oauth.UserResDto;
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
    private final HttpServletResponse response;
    private final MemberRepository memberRepository;

    public void getRedirectURL(PlatformType platformType) throws IOException {
        String redirectURL;

        switch (platformType) {
            case GOOGLE -> {
                redirectURL = googleOAuth.getOAuthRedirectURL();
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
                GoogleUser googleUser = googleOAuth.getGoogleUserInfo(userInfoResponse);
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
            default -> throw new IllegalArgumentException("알 수 없는 로그인 형식입니다.");
        }
    }
}
