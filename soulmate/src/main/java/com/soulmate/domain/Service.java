package com.soulmate.domain;

import com.soulmate.domain.BaseTimeEntity;
import com.soulmate.domain.Category;
import com.soulmate.domain.Designer;
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
public class Service extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    private int price;

    private String option;

    @Lob
    private String desc;

    @OneToMany(mappedBy = "service")
    private List<DesignerService> designerServices = new ArrayList<>();

    @Builder
    public Service(Long id, Category category, String name, int price, String option, String desc) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.option = option;
        this.desc = desc;
    }
}
