package com.soulmate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulmate.domain.Member;
import com.soulmate.domain.enums.PlatformType;
import com.soulmate.domain.enums.Role;
import com.soulmate.domain.repository.MemberRepository;
import com.soulmate.web.dto.oauth.UserResDto;
import com.soulmate.web.dto.request.PetReqDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    TestRestTemplate restTemplate;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    protected MockHttpSession session;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        //회원 생성하기
        Member mockMember = memberRepository.save(Member.builder().name("회원").role(Role.USER).platformType(PlatformType.GOOGLE).build());
        session = new MockHttpSession();
        session.setAttribute("user", new UserResDto(mockMember));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void Pet_등록된다 () throws Exception {
        //given
        PetReqDto pet = PetReqDto.builder()
                .name("")
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

        //then
        mvc.perform(
                multipart(url)
                    .file(file)
                    .file(metadata)
                    .session(session))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
}