package com.soulmate.web.dto.request;

import com.soulmate.domain.Pet;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @ToString
public class PetReqDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "품종은 필수 입력 값입니다.")
    private String kind;

    @Min(value = 0, message = "몸무게는 0 이상이어야 합니다.")
    private int weight;

    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;

    private String sex;

    private Boolean neutral;

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
