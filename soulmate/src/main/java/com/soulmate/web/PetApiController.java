package com.soulmate.web;

import com.soulmate.config.auth.LoginUser;
import com.soulmate.config.auth.dto.SessionUser;
import com.soulmate.service.PetService;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.ErrorResDto;
import com.soulmate.web.dto.response.PetResDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            @LoginUser SessionUser user,
            BindingResult bindingResult,
            HttpServletRequest request
    ) throws IOException {
        /*
        HttpSession session = request.getSession();
        UserResDto loginUser = (UserResDto) session.getAttribute("user");
         */

        //System.out.println("logined : " + loginUser);
        System.out.printf("params: %s, file: %s\n", params.toString(), file.getOriginalFilename());

        //사용자 정보 없음
        if (user == null) {
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

        petService.register(params, file, user);

        return ResponseEntity.ok("success");
    }

    /**
     * 회원의 펫을 모두 조회한다.
     */
    @GetMapping("")
    public ResponseEntity findAll(
            @LoginUser SessionUser user
    ) {
        //사용자 정보 없음
        if (user == null) {
            return ResponseEntity.badRequest().body("로그인 후 이용 가능합니다.");
        }

        List<PetResDto> response = petService.findAll(user);

        return ResponseEntity.ok(response);
    }
}
