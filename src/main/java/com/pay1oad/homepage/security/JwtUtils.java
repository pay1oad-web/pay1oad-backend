package com.pay1oad.homepage.security;

import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    public String getAccountIdFromRequest(String authorizationHeader) {
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        Member member=memberService.getMemberByID(userid);

        return member.getUsername();
    }
}
