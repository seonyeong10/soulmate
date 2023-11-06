package com.soulmate.service;

import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.enums.Role;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.domain.repository.PetRepository;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.PetResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class PetServiceTest {
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private PetService petService;

    private Member member;

    @BeforeEach
    public void setup() {
        member = memberRepository.saveAndFlush(Member.builder().name("회원").email("1@2.com").platformType(PlatformType.GOOGLE).role(Role.USER).build());
    }
    
    @Test
    public void register_test () throws Exception {
        //given
        PetReqDto request = PetReqDto.builder()
                .name("테스트")
                .age(1)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .neutral(false)
                .weight(5)
                .build();

        MultipartFile file = new MockMultipartFile("file", "pet.txt", MediaType.TEXT_PLAIN_VALUE, ":)".getBytes());
        
        //when
        Long savedId = petService.register(request, file, new SessionUser(member));
        Pet pet = petRepository.findById(savedId).orElseThrow(() -> new NoSuchElementException("반려동물이 존재하지 않습니다. pet_id = " + savedId));

        //then
        assertEquals(savedId, pet.getId());
        assertEquals(request.getName(), pet.getName());
        assertEquals(request.getKind(), pet.getKind());
    }

    @Test
    public void pet_findAll_test () throws Exception {
        //given
        Pet saved = Pet.builder()
                .name("쵸파")
                .age(1)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .neutral(false)
                .weight(5)
                .build();
        saved.addGuardian(member);

        petRepository.save(saved);

        //when
        List<PetResDto> all = petService.findAll(new SessionUser(member));

        //then
        assertEquals(saved.getName(), all.get(0).getName());
        assertEquals(saved.getKind(), all.get(0).getKind());
    }

    @Test
    public void pet_update () throws Exception {
        //given
        Pet saved = Pet.builder()
                .name("쵸파")
                .age(1)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .neutral(false)
                .weight(5)
                .build();
        saved.addGuardian(member);
        MultipartFile file = new MockMultipartFile("file", "update.txt", MediaType.TEXT_PLAIN_VALUE, ":)".getBytes());

        petRepository.save(saved);

        //when
        PetReqDto request = PetReqDto.builder()
                .name("수정")
                .age(0)
                .build();

        petService.update(saved.getId(), new SessionUser(member), request, file);

        //then
        Pet find = petRepository.findById(saved.getId()).orElseThrow(() -> new NoSuchElementException("반려동물이 존재하지 않습니다. pet_id = " + saved.getId()));
        assertEquals(request.getName(), find.getName());
        assertEquals(request.getAge(), find.getAge());
        assertEquals(file.getOriginalFilename(), find.getAttachFiles().get(0).getOriginalName());
    }

    @Test
    public void pet_delete () throws Exception {
        //given
        Pet saved = Pet.builder()
                .name("쵸파")
                .age(1)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .neutral(false)
                .weight(5)
                .build();
        saved.addGuardian(member);

        petRepository.save(saved);

        //when
        petService.delete(saved.getId(), member.getId());

        //then
        List<Pet> all = petRepository.findAllByMemberId(member.getId());

        assertEquals(0, all.size());
    }
}