package com.soulmate.domain;

import com.soulmate.domain.attachFile.PetAttachFile;
import com.soulmate.web.dto.request.PetReqDto;
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
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String name;
    private String kind;
    private int weight;
    private int age;
    private String sex;

    //부모가 삭제될 때 함께 삭제하고, 연관관계가 끊어진 엔티티는 삭제
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetAttachFile> attachFiles = new ArrayList<>();

    @Builder
    public Pet(Long id, Member member, String name, String kind, int weight, int age, String sex, String photo) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.kind = kind;
        this.weight = weight;
        this.age = age;
        this.sex = sex;
    }

    public void update(PetReqDto request) {
        name = request.getName();
        kind = request.getKind();
        weight = request.getWeight();
        age = request.getAge();
        sex = request.getSex();
    }

    //== 연관관계 메서드 ==//
    public void addGuardian(Member member) {
        this.member = member;
    }

    public void addFile(PetAttachFile file) {
        attachFiles.add(file);
    }
}
