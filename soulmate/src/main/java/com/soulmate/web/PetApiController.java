package com.soulmate.web;

import com.soulmate.service.PetService;
import com.soulmate.web.dto.oauth.UserResDto;
import com.soulmate.web.dto.request.PetReqDto;
import com.soulmate.web.dto.response.ResultResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my/pets")
public class PetApiController {

    private final PetService petService;

    @PostMapping("/edit")
    public ResultResDto register(
            @RequestBody PetReqDto params,
            @RequestPart List<MultipartFile> files,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        UserResDto logined = (UserResDto) session.getAttribute("user");

        //petService.register(params, files, logined.getId());

        return null;
    }
}
