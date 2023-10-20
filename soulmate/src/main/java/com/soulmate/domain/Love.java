package com.soulmate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private StyleBook styleBook;
    private int loves; //0: false 1: true

    @Builder
    public Love(Long id, Member member, StyleBook styleBook, int loves) {
        this.id = id;
        this.member = member;
        this.styleBook = styleBook;
        this.loves = loves;
    }

    //== 연관관계 메서드 ==//
    public void addLike(int like) {
        if (like == 0) {
            styleBook.addLoves();
        } else {
            styleBook.disLoves();
        }

        this.loves = like;
    }
}
