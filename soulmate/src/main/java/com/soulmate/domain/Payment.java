package com.soulmate.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private String method;
    private int totalPrice;

    public Payment(Long id, String method, int totalPrice) {
        this.id = id;
        this.method = method;
        this.totalPrice = totalPrice;
    }
}
