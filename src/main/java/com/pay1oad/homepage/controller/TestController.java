package com.pay1oad.homepage.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "sbb on load";
    }

    @GetMapping("/sbc")
//    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String idex() {
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .filter(a -> a.getAuthority().equals("ROLE_ADMIN"))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Access denied"));
        return "sbb client on load";
    }

    @GetMapping("/sbu")
//    @PreAuthorize("hasRole('UNAUTH')")
    @ResponseBody
    public String idx() {
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .filter(a -> a.getAuthority().equals("ROLE_UNAUTH"))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Access denied"));
        return "sbb unauth on load";
    }
}
