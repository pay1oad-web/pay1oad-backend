package com.pay1oad.homepage.security;

import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    public String getAccountIdFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            String token = bearerToken.substring(7);

            int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(token));
            Member member=memberService.getMemberByID(userid);

            return member.getUsername();
        }else{
            return null;
        }
    }

    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }else{
            return null;
        }
    }
}
