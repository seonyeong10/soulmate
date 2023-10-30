package com.soulmate.web;

import com.soulmate.service.PetService;
import com.soulmate.web.dto.oauth.UserResDto;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.ResultResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import java.io.IOException;
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
    public ResponseEntity<Object> register(
            @RequestPart(name = "pet") PetReqDto params,
            @RequestPart(name = "file", required = false) MultipartFile file,
            BindingResult bindingResult,
            HttpServletRequest request
    ) throws IOException {
        //유효성 검사하기
        validator.validate(params, bindingResult);

        HttpSession session = request.getSession();
        UserResDto loginUser = (UserResDto) session.getAttribute("user");

        System.out.println("logined : " + loginUser);

        if (bindingResult.hasErrors()) {
            for (ObjectError err : bindingResult.getAllErrors()) {
                log.info(err.getObjectName() + err.getDefaultMessage());

                //사용자에게 보여줄 메시지 처리하기
            }

            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        if (loginUser == null) {
            return ResponseEntity.badRequest().body("로그인 후 이용 가능합니다.");
        }

        petService.register(params, file, loginUser.getId());

        return ResponseEntity.ok("success");
    }
}
