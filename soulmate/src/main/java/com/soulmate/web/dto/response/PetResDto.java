package com.soulmate.web.dto.response;

import com.soulmate.domain.Pet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@ToString
public class PetResDto {
    private Long id;
    private String name;

    private String kind;

    private int weight;

    private int age;

    private String sex;

    private String desc;

    private List<Long> files = new ArrayList<>();

    public PetResDto(Pet pet) {
        id = pet.getId();
        name = pet.getName();
        kind = pet.getKind();
        weight = pet.getWeight();
        age = pet.getAge();
        sex = pet.getSex();
        desc = pet.getDesc();
        pet.getAttachFiles().forEach(at -> files.add(at.getId()));
    }
}
