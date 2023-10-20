package com.soulmate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StyleBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    private Designer designer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    private String name;
    private int length;
    private int curly;
    @Lob
    private String desc;
    private int loves;

    @Builder
    public StyleBook(Long id, Designer designer, Category category, String name, int length, int curly, String desc, int loves) {
        this.id = id;
        this.designer = designer;
        this.category = category;
        this.name = name;
        this.length = length;
        this.curly = curly;
        this.desc = desc;
        this.loves = loves;
    }

    //== 연관관계 메서드 ==//
    public void addLoves() {
        ++loves;
    }

    public void disLoves() {
        --loves;
    }
}
