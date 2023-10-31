package com.soulmate.service;

import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;
import com.soulmate.domain.attachFile.PetAttachFile;
import com.soulmate.domain.repository.AttachFileRepository;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.domain.repository.PetRepository;
import com.soulmate.web.dto.request.PetReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    private final MemberRepository memberRepository;

    private final AttachFileRepository fileRepository;

    private final FileUtil fileUtil;

    public Long register(PetReqDto request, MultipartFile file, Long memberId) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다. member_id = " + memberId));

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
}
