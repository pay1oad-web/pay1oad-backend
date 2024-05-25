package com.pay1oad.homepage.security;

import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.login.JwtRedisService;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtRedisService jwtRedisService;

    @Autowired
    private MemberService memberService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/auth/signup", "/auth/signin", "/verify/email"};
        // 제외할 url 설정
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            //log.info("request "+ request);
            String token=parseBearerToken(request);
            log.info("Filter is runnung...");
            //log.info("Token "+token.replaceAll("[\r\n]",""));

            if(token!=null&&!token.equalsIgnoreCase("null")){
                //validate login
                String userID=tokenProvider.validateAndGetUserId(token);
                Member member = memberService.getMemberByID(Integer.valueOf(userID));
                //log.info("Userid: "+userID);
                String Username= String.valueOf(memberService.getUsername(Integer.valueOf(userID)));
                //log.info("Username: "+Username);
                if(Objects.equals(jwtRedisService.getValues(Username), token)){//jwtRedisService.getJwtListByJwt(token)
                    log.info("Authenticated user Name"+userID.replaceAll("[\r\n]",""));
                    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    		member,
                            null,
                            AuthorityUtils.NO_AUTHORITIES
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                }else{
                    log.info("Logged out JWT");
                }
            }else{
                log.info("Authentication faild in JWT");
            }

        }catch(Exception ex){
            logger.error("Could not set user auth in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }


}