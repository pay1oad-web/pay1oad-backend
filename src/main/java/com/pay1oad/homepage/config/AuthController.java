package com.pay1oad.homepage.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/checkAuthAndRedirect")
    public String checkAuthAndRedirect() {
        // 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 인증되어 있지 않으면 예외를 던져서 처리
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Authentication", "User", "User is not authenticated");
        }

        // 사용자가 인증되어 있으면 "redirect:/dashboard"로 리다이렉트
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        // 대시보드 페이지로 이동
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        // 로그인 페이지로 이동
        return "login";
    }
}
