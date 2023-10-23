package com.soulmate.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthController {
    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }
}
