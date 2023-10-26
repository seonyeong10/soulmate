package com.soulmate.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/auth/fail")
    public String result(
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "desc", required = false) String desc,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("desc", desc);
        return "authFail";
    }
}
