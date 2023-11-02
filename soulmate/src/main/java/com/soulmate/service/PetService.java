package com.soulmate.service;

import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;
import com.soulmate.domain.attachFile.PetAttachFile;
import com.soulmate.domain.repository.AttachFileRepository;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.domain.repository.PetRepository;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.PetResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    private final MemberRepository memberRepository;

    private final AttachFileRepository fileRepository;

    private final FileUtil fileUtil;

    /**
     * 반려동물을 등록한다.
     * @param request 반려동물 메타데이터
     * @param file 첨부파일(이미지)
     * @param user 로그인 사용자
     * @return
     * @throws IOException
     */
    public Long register(PetReqDto request, MultipartFile file, SessionUser user) throws IOException {
        Member member = memberRepository.findById(user.getId()).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다. member_id = " + user.getId()));

        Pet pet = request.toPetEntity();
        pet.addGuardian(member);

        Pet saved = petRepository.save(pet);

        if (file != null) {
            //첨부파일 등록하기
            PetAttachFile petAttachFile = fileUtil.uploadOne(file, "pet").toPetFile();
            petAttachFile.addPet(saved);

            fileRepository.save(petAttachFile);
        }

        return saved.getId();
    }

    /**
     * 모든 반려동물을 조회한다.
     * @param user
     * @return
     */
    public List<PetResDto> findAll(SessionUser user) {
        return petRepository.findAllByMemberId(user.getId()).stream()
                .map(PetResDto::new)
                .toList();
    }

    public PetResDto findOne(Long petId, SessionUser user) {
        return petRepository.findOneByMemberId(petId, user.getId()).stream()
                .findAny().map(PetResDto::new)
                .orElseThrow(() -> new NoSuchElementException(String.format("반려동물을 찾을 수 없습니다. pet_id = %d", petId)));
    }
}
