package com.soulmate.web.dto.oauth;

import com.soulmate.domain.Member;
import com.soulmate.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
public class UserResDto {
    private Long id;

    private String name;

    private String nickname;

    private Role role;

    public UserResDto(Member member) {
        id = member.getId();
        name = member.getName();
        nickname = member.getNickname();
        role = member.getRole();
    }
}
