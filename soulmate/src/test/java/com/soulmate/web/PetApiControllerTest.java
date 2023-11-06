package com.soulmate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.domain.Member;
import com.soulmate.domain.Pet;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.enums.Role;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.domain.repository.PetRepository;
import com.soulmate.web.dto.request.PetReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PetApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    WebApplicationContext context;

    private Member mockMember;

    private MockMvc mvc;

    protected MockHttpSession session;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        //회원 생성하기
        mockMember = memberRepository.save(Member.builder().name("회원").role(Role.USER).platformType(PlatformType.GOOGLE).build());
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(mockMember));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Pet_등록된다 () throws Exception {
        //given
        PetReqDto pet = PetReqDto.builder()
                .name("쵸파")
                .age(1)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .weight(5)
                .build();
        MockMultipartFile file = new MockMultipartFile("file", "pet.txt", MediaType.TEXT_PLAIN_VALUE, ":)".getBytes());

        String petJson = new ObjectMapper().writeValueAsString(pet);
        MockMultipartFile metadata = new MockMultipartFile("pet", "", "application/json", petJson.getBytes());

        String url = "http://localhost:" + port + "/api/v1/my/pets/edit";

        //when
        /*
        HttpHeaders headers = new HttpHeaders();

        //MultiValueMap 은 MultipartFile 데이터가 포함되면 자동으로 header에 multipart/form-data 를 저장한다.
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("pet", pet);

        //ByteArrayResource로 변경하지 않으면 Error
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        multiValueMap.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap);

        //ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        */

        mvc.perform(
                multipart(url)
                    .file(file)
                    .file(metadata)
                    .session(session))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        //then
        List<Pet> all = petRepository.findAllByMemberId(mockMember.getId());

        assertEquals(all.get(0).getName(), pet.getName());
        assertEquals(all.get(0).getMember().getId(), mockMember.getId());
    }

    @Test
    @WithMockUser("USER")
    @Transactional
    public void Pet_수정된다 () throws Exception {
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
        saved.addGuardian(mockMember);
        Long savedId = petRepository.save(saved).getId();

        PetReqDto pet = PetReqDto.builder()
                .name("수정후")
                .age(3)
                .desc("테스트 중입니다.")
                .kind("말티즈")
                .sex("F")
                .neutral(false)
                .weight(5)
                .build();
        MockMultipartFile file = new MockMultipartFile("file", "pet.txt", MediaType.TEXT_PLAIN_VALUE, ":)".getBytes());

        String petJson = new ObjectMapper().writeValueAsString(pet);
        MockMultipartFile metadata = new MockMultipartFile("pet", "", "application/json", petJson.getBytes());

        String url = "http://localhost:" + port + "/api/v1/my/pets/edit/" + saved.getId();

        //when
        mvc.perform(
                multipart(url)
                        .file(file)
                        .file(metadata)
                        .session(session))
                .andDo(print())
                .andExpect(status().isOk())
        ;

        //then
        Pet find = petRepository.findOneByMemberId(saved.getId(), mockMember.getId()).orElseThrow(() -> new NoSuchElementException("반려동물이 존재하지 않습니다. pet_id = " + savedId));

        assertEquals(pet.getName(), find.getName());
        assertEquals(pet.getAge(), find.getAge());
        assertEquals(file.getOriginalFilename(), find.getAttachFiles().get(0).getOriginalName());
    }

    @Test
    @WithMockUser("USER")
    @Transactional
    public void Pet_삭제된다 () throws Exception {
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
        saved.addGuardian(mockMember);
        Long savedId = petRepository.save(saved).getId();

        String url = "http://localhost:" + port + "/api/v1/my/pets/" + saved.getId();

        //when
        mvc.perform(
                delete(url)
                .session(session)
        ).andDo(print())
         .andExpect(status().isOk());

        //then
        List<Pet> all = petRepository.findAllByMemberId(mockMember.getId());

        assertEquals(0, all.size());
    }
}