package com.soulmate.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/auth/logout")
    public String logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
