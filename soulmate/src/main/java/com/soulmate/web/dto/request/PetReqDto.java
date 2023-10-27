package com.soulmate.web.dto.request;

import com.soulmate.domain.Pet;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PetReqDto {
    private String name;

    private String kind;

    private int weight;

    private int age;

    private String sex;

    private boolean neutral;

    private String desc;

    @Builder
    public PetReqDto(String name, String kind, int weight, int age, String sex, boolean neutral, String desc) {
        this.name = name;
        this.kind = kind;
        this.weight = weight;
        this.age = age;
        this.sex = sex;
        this.neutral = neutral;
        this.desc = desc;
    }

    public Pet toPetEntity() {
        return Pet.builder()
                .name(name)
                .kind(kind)
                .weight(weight)
                .age(age)
                .sex(sex)
                .neutral(neutral)
                .desc(desc)
                .build();
    }
}
