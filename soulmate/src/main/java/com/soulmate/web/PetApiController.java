package com.soulmate.web;

import com.soulmate.service.PetService;
import com.soulmate.web.dto.oauth.UserResDto;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.ErrorResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my/pets")
public class PetApiController {

    private final PetService petService;

    private final Validator validator;

    @PostMapping("/edit")
    public ResponseEntity register(
            @RequestPart(name = "pet") PetReqDto params,
            @RequestPart(name = "file", required = false) MultipartFile file,
            BindingResult bindingResult,
            HttpServletRequest request
    ) throws IOException {
        HttpSession session = request.getSession();
        UserResDto loginUser = (UserResDto) session.getAttribute("user");

        //System.out.println("logined : " + loginUser);
        System.out.printf("params: %s, file: %s\n", params.toString(), file.getOriginalFilename());

        //사용자 정보 없음
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("로그인 후 이용 가능합니다.");
        }

        //유효성 검사하기
        validator.validate(params, bindingResult);

        //유효하지 않은 파라미터
        if (bindingResult.hasErrors()) {
            List<ErrorResDto> errors = new ArrayList<>();

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError fieldError = (FieldError) objectError;

                errors.add(ErrorResDto.builder()
                                .objectName(fieldError.getObjectName())
                                .field(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build());
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        petService.register(params, file, loginUser.getId());

        return ResponseEntity.ok("success");
    }
}
