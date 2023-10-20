package com.soulmate.domain;

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
public class Category extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    private String name;
    private String engName;
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Builder
    public Category(Long id, Category parent, String name, String engName, List<Category> children) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.engName = engName;
        this.children = children;
    }

    //== 연관관계 메서드 ==//
    /**
     * 상위 카테고리를 저장한다.
     */
    public void addParentCategory (Category category) {
        this.parent = category;
    }
}
