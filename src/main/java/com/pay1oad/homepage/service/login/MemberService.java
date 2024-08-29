package com.pay1oad.homepage.service.login;
import com.pay1oad.homepage.dto.JwtToken;
import com.pay1oad.homepage.dto.login.LoginRequestDTO;
import com.pay1oad.homepage.dto.login.LoginResponseDTO;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.MemberAuth;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import com.pay1oad.homepage.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtRedisService jwtRedisService;
    private final TokenProvider tokenProvider;

    public Member create(final Member member) {
        if (member == null || member.getUsername() == null) {
            throw new RuntimeException("Invalid Arguments");
        }
        final String username = member.getUsername();
        if (memberRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username.replaceAll("[\r\n]", ""));
            throw new RuntimeException("Username already exists");
        }

        member.setMemberAuth(MemberAuth.UNAUTH);

        return memberRepository.save(member);
    }

    public Member save(final Member member){
        return memberRepository.save(member);
    }

    public Member getByCredentials(final String username, final String passwd){
        return memberRepository.findByUsernameAndPasswd(username, passwd);
    }

    public Member checkID(final String username){
        return memberRepository.findByUsername(username);
    }

    public String getUsername(final Integer userid){
        Member member=memberRepository.findByUserid(userid);
        if(member!=null){
            return member.getUsername();
        }else{
            return null;
        }
    }

    public Member getMemberByID(final Integer userid){ return memberRepository.findByUserid(userid);}


    public LoginResponseDTO.toSignUpDTO signIn(LoginRequestDTO.toSignInDTO signInDTO){
        Member member=getByCredentials(
                signInDTO.getUserName(),
                signInDTO.getPasswd());

        //log.info(memberDTO.getUsername()+"\n"+memberDTO.getPasswd()+"\n");
        if(member!=null){
            final JwtToken token=tokenProvider.create(member);

            final LoginResponseDTO.toSignUpDTO toSignUpDTO = LoginResponseDTO.toSignUpDTO.builder()
                    .userName(member.getUsername())
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .build();

            //redis

            //Delete exist refresh Token
            if(jwtRedisService.getValues(signInDTO.getUserName())!=null){
                jwtRedisService.deleteValues(signInDTO.getUserName());
            }

            //add logged in list
//            jwtRedisService.setValues("access_"+member.getUsername(), token.getAccessToken(), Duration.ofSeconds(600));
            jwtRedisService.setValues(member.getUsername(), token.getRefreshToken(), Duration.ofHours(60));

            return toSignUpDTO;
        }else{
            throw new CustomException(ErrorStatus.LOGIN_FAILED_BY_PASSWD_OR_MEMBER_NOT_EXIST);
        }
    }
}
