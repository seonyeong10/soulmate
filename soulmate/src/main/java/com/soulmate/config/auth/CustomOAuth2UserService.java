package com.soulmate.config.auth;

import com.soulmate.config.auth.dto.OAuthAttributes;
import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.domain.Member;
import com.soulmate.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * SNS 로그인 이후 가져온 사용자의 정보(email, name, picture emd)을 기반으로
 * 가입 및 정보수정, 세션 저장 등의 기능 지원
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /**
         * registrationId: 현재 로그인 진행 중인 서비스를 구분 (구글, 네이버, 카카오 등을 구분한다.)
         * userNameAttributeName
         *  - OAuth2 로그인 진행 시 키가 되는 필드값 (PK)
         *  - 구글의 경우 기본적으로 코드를 지원(default = sub)하지만, 네이버 카카오 등은 기본 지원하지 않는다.
         *  - 구글 외 서비스와 동시 지원할 때 사용
         * OAuthAttributes
         *  - OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담은 클래스
         * SessionUser
         *  - 세션에 사용자 정보를 저장하기 위한 dto 클래스
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //현재 로그인 중인 플랫폼 구분
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //OAuth2 로그인 진행 시 키가 되는 필드 값

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);
        session.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    public Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }
}
