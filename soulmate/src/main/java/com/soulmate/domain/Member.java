package com.soulmate.domain;

import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.enums.Role;
import com.soulmate.web.dto.oauth.UserResDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String mobile;

    private String picture;

    @Enumerated(EnumType.STRING)
    private PlatformType platformType; //enum

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; //권한

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String name, String nickname, String mobile, String picture, PlatformType platformType, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.mobile = mobile;
        this.picture = picture;
        this.platformType = platformType;
        this.role = role;
    }

    public void addRole(Role role) {
        this.role = role;
    }
}
