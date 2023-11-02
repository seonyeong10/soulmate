package com.soulmate.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.soulmate.domain.QPet.pet;
//import static com.soulmate.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Pet> findAllByMemberId(Long memberId) {
        return queryFactory.selectFrom(pet)
                .where(
                        pet.member.id.eq(memberId)
                )
                .fetch();
    }
}
