package com.soulmate.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.service.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OAuthRestController {
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
     */
    @GetMapping("/api/v1/auth/code/{platform}/callback")
    public String callback(
            @PathVariable(name = "platform") String platform,
            @RequestParam(name = "code") String code
    ) throws JsonProcessingException {
        PlatformType platformType = PlatformType.valueOf(platform.toUpperCase());

        return oAuthService.oAuthLogin(platformType, code);
    }
}
