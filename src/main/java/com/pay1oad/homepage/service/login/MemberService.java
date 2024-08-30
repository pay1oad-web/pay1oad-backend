package com.pay1oad.homepage.service.login;
import com.pay1oad.homepage.dto.JwtToken;
import com.pay1oad.homepage.dto.login.LoginRequestDTO;
import com.pay1oad.homepage.dto.login.LoginResponseDTO;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.MemberAuth;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import com.pay1oad.homepage.security.JwtUtils;
import com.pay1oad.homepage.security.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtRedisService jwtRedisService;
    private final TokenProvider tokenProvider;
    private final JwtUtils jwtUtils;

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


    public LoginResponseDTO.toSignInDTO signIn(LoginRequestDTO.toSignInDTO signInDTO){
        Member member=getByCredentials(
                signInDTO.getUserName(),
                signInDTO.getPasswd());

        //log.info(memberDTO.getUsername()+"\n"+memberDTO.getPasswd()+"\n");
        if(member!=null){
            final JwtToken token=tokenProvider.create(member);

            final LoginResponseDTO.toSignInDTO toSignInDTO = LoginResponseDTO.toSignInDTO.builder()
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

            return toSignInDTO;
        }else{
            throw new CustomException(ErrorStatus.LOGIN_FAILED_BY_PASSWD_OR_MEMBER_NOT_EXIST);
        }
    }

    public LoginResponseDTO.toSignUpDTO signUp(LoginRequestDTO.toSignUpDTO toSignUpDTO){

        if (toSignUpDTO==null||toSignUpDTO.getPasswd()==null){
            throw new CustomException(ErrorStatus.PASSWD_FORMAT_NOT_VALID);
        }else if(!validPasswd(toSignUpDTO.getPasswd())){
            throw new CustomException(ErrorStatus.PASSWD_FORMAT_NOT_VALID);
        }else if (!validEmail(toSignUpDTO.getEmail())) {
            throw new CustomException(ErrorStatus.EMAIL_FORMAT_NOT_VALID);
        }

        Optional<Member> ckMember = Optional.ofNullable(memberRepository.findByUsername(toSignUpDTO.getUserName()));

        if(ckMember.isPresent()){
            throw new CustomException(ErrorStatus.DUPLICATED_USERNAME);
        }

        Member member =Member.builder()
                .username(toSignUpDTO.getUserName())
                .passwd(toSignUpDTO.getPasswd())
                .email(toSignUpDTO.getEmail())
                .verified(Boolean.FALSE)
                .build();

        Member resisteredByMember=create(member);

        LoginResponseDTO.toSignUpDTO responseSignUpDTO=LoginResponseDTO.toSignUpDTO.builder()
                .userName(resisteredByMember.getUsername())
                .email(resisteredByMember.getEmail())
                .build();

        return responseSignUpDTO;

    }

    public String signOut(HttpServletRequest httpServletRequest){
        String accessToken = jwtUtils.getToken(httpServletRequest);
        if (accessToken != null && !accessToken.equalsIgnoreCase("null")) {

            Claims claims = tokenProvider.parseClaims(accessToken);
            if (claims.get("auth") == null) {
                throw new CustomException(ErrorStatus.NOT_ACCESS_TOKEN);
            }
        }else{
            throw new CustomException(ErrorStatus.CANNOT_FOUND_ACCESS_TOKEN);
        }

        Integer userId = jwtUtils.getUserId(httpServletRequest);
        String userName = memberRepository.findByUserid(userId).getUsername();
        jwtRedisService.deleteValues(userName);

        Long expiration = tokenProvider.getExpiration(accessToken);
        jwtRedisService.setBlackList(accessToken, "access_token", expiration);
        return "signed out: "+userName;
    }

    public LoginResponseDTO.toRefreshDTO refreshToken(HttpServletRequest httpServletRequest){
        //get token
        String refreshToken = jwtUtils.getToken(httpServletRequest);
        //log.info("token: "+token);

        //get username
        int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(refreshToken));
        Member member = memberRepository.findByUserid(userid);
        String username=member.getUsername();

        if (Objects.equals(jwtRedisService.getValues(username), refreshToken)) {
            log.info("Refreshed: "+username.replaceAll("[\r\n]",""));

            jwtRedisService.deleteValues(username);


            final JwtToken token=tokenProvider.create(member);

            //refresh
            jwtRedisService.setValues(username, token.getRefreshToken(), Duration.ofDays(3));

            return LoginResponseDTO.toRefreshDTO.builder()
                    .userName(username)
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .build();
        }else{
            throw new CustomException(ErrorStatus.REFRESH_TOKEN_NOT_VALID);
        }
    }







    private boolean validPasswd(String passwd){
        if(passwd.length() < 8)
            return false;

        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        return passwd.matches(pattern);
    }

    private boolean validEmail(String email){
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        return email.matches(pattern);
    }

}
