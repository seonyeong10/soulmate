package com.soulmate.config.auth.dto;

import com.soulmate.domain.Member;
import lombok.Getter;

import java.io.Serializable;

/**
 * 세션에 저장하기 위한 직렬화된 dto
 * 인증된 사용자 정보만 필요
 */

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String picture;

    public SessionUser(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
    }
}