package com.soulmate.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.service.oauth.OAuthService;
import com.soulmate.web.dto.oauth.UserResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuthApiController {
    private final OAuthService oAuthService;

    @GetMapping("/api/v1/auth/code/{platform}")
    public void socialLoginRedirect(
            @PathVariable(name = "platform") String platform
    ) throws IOException {
        PlatformType platformType = PlatformType.valueOf(platform.toUpperCase());

        oAuthService.getRedirectURL(platformType);
    }

    /**
     * 소셜 로그인 이후
     * SNS Login API server 요청에 의한 callback 처리하기
     * 세션 등록하기
     */
    @GetMapping("/api/v1/auth/code/{platform}/callback")
    public void callback(
            @PathVariable(name = "platform") String platform,
            @RequestParam(name = "code") String code,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws JsonProcessingException, IOException {
        PlatformType platformType = PlatformType.valueOf(platform.toUpperCase());

        UserResDto loginUser = oAuthService.oAuthLogin(platformType, code);

        //세션 저장하기
        request.getSession().invalidate(); //세션 초기화
        HttpSession session = request.getSession(true); //session이 없으면 생성
        session.setAttribute("user", loginUser);
        session.setMaxInactiveInterval(1800); //30분

        //리디렉션
        response.sendRedirect("/");
    }
}
