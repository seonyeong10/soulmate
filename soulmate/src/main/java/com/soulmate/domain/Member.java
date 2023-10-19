package com.soulmate.domain;

import com.soulmate.domain.enums.PlatformType;
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
    private String nick;
    private String mobile;
    @Enumerated(EnumType.STRING)
    private PlatformType platformType; //enum
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String name, String nick, String mobile, PlatformType platformType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nick = nick;
        this.mobile = mobile;
        this.platformType = platformType;
    }
}
