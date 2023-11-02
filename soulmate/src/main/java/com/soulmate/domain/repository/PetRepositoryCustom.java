package com.soulmate.domain.repository;

import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;

import java.util.List;

public interface PetRepositoryCustom {

    /**
     * 회원의 모든 반려동물 조회하기
     * @return
     */
    List<Pet> findAllByMemberId(Long memberId);
}
