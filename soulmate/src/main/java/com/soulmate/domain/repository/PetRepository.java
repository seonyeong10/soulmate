package com.soulmate.domain.repository;

import com.soulmate.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {

    @Query("select p from Pet p where p.id = :pet_id and p.member.id = :member_id")
    public Optional<Pet> findOneByMemberId(@Param(value = "pet_id") Long petId, @Param(value = "member_id") Long memberId);
}
