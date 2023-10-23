package com.soulmate.web.dto.oauth;

import com.soulmate.domain.Member;

public interface OAuthUser {
    /**
     * 신규 회원을 등록한다.
     */
    Member toMember();
}
