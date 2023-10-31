package com.soulmate.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/my")
    public String myPage() {
        return "my/my";
    }
}
