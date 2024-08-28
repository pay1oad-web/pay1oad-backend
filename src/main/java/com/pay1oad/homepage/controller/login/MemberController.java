package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.dto.JwtToken;
import com.pay1oad.homepage.dto.login.LoginRequestDTO;
import com.pay1oad.homepage.dto.login.MemberDTO;
import com.pay1oad.homepage.dto.ResponseDTO;
//import com.pay1oad.homepage.event.UserRegistrationEvent;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.login.JwtRedisService;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    private final JwtRedisService jwtRedisService;



    /*private final JavaMailSender mailSender;
    private final EmailVerificationService verificationService;

    @Autowired
    public MemberController(JavaMailSender mailSender, EmailVerificationService verificationService) {
        this.mailSender = mailSender;
        this.verificationService = verificationService;
    }*/

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        try {
            if (memberDTO==null||memberDTO.getPasswd()==null){
                throw new RuntimeException("Passwd value null");
            }else if(!validPasswd(memberDTO.getPasswd())){
                throw new RuntimeException("invalid Passwd value");
            }else if (!validEmail(memberDTO.getEmail())) {
                throw new RuntimeException("invalid Email");
            }

            Member member =Member.builder()
                    .username(memberDTO.getUsername())
                    .passwd(memberDTO.getPasswd())
                    .email(memberDTO.getEmail())
                    .build();

            Member resisteredByMember=memberService.create(member);
            MemberDTO responseMemberDTO=MemberDTO.builder()
                    .userid(String.valueOf(resisteredByMember.getUserid()))
                    .username(resisteredByMember.getUsername())
                    .email(resisteredByMember.getEmail())
                    .build();

            return ResponseEntity.ok().body(responseMemberDTO);

        }catch(Exception e){
            ResponseDTO responseDTO=ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequestDTO.toSignInDTO signInDTO){
        Member member=memberService.getByCredentials(
                signInDTO.getUserName(),
                signInDTO.getPasswd());

        //log.info(memberDTO.getUsername()+"\n"+memberDTO.getPasswd()+"\n");
        if(member!=null){
            final JwtToken token=tokenProvider.create(member);
            final MemberDTO responseMemberDTO = MemberDTO.builder()
                    .username(member.getUsername())
                    .userid(String.valueOf(member.getUserid()))
                    .email(member.getEmail())
                    .token(String.valueOf(token))
                    .build();

            //redis

            //Already signed in
            if(jwtRedisService.getValues(signInDTO.getUserName())!=null){
                jwtRedisService.deleteValues(signInDTO.getUserName());
            }

            //add logged in list
            jwtRedisService.setValues("access_"+member.getUsername(), token.getAccessToken(), Duration.ofSeconds(600));
            jwtRedisService.setValues("refresh_"+member.getUsername(), token.getRefreshToken(), Duration.ofHours(60));

            return ResponseEntity.ok().body(responseMemberDTO);
        }else{
            throw new CustomException(ErrorStatus.LOGIN_FAILED_BY_PASSWD_OR_MEMBER_NOT_EXIST);
        }

    }

    @PostMapping("/signout")
    public String signout(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String userid;

            if (principal instanceof UserDetails) {
                userid = String.valueOf(((Member) principal).getUserid());
            } else if (principal instanceof String) {
                userid = (String) principal;
            } else {
                throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
            }



            //log.info("userid in signout: "+userid);
            if(!Objects.equals(userid, "anonymousUser")){
//                log.info("username in signout: "+username.replaceAll("[\r\n]",""));

                //logout
                jwtRedisService.deleteValues(userid);

                return "signed out: "+userid;
            }else{
                return "anonymousUser";
            }


        } else {
            // 인증 정보가 없는 경우 처리
            System.out.println("인증 정보 없음");
            return "인증 정보 없음";
        }

    }

    @PostMapping("/refreshToken")
    public String refresh(@RequestHeader("Authorization") String authorizationHeader){
        //get token
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }else{
            return "Toekn is not valid";
        }
        //log.info("token: "+token);

        //get username
        int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(token));
        String username=memberService.getUsername(userid);


        log.info("Refreshed: "+username.replaceAll("[\r\n]",""));

        //refresh
        jwtRedisService.setValues(username, token, Duration.ofSeconds(600));

        return "refreshed: "+username;

    }

//    @GetMapping("/test")
//    public String getSbbText() {
//        return "sbb";
//    }
//
//    @PostMapping("/test")
//    public String postToSbb() {
//        return "sbb";
//    }

    private String getText(Member member, String verificationId) {
        String encodedVerificationId = new String(Base64.getEncoder().encode(verificationId.getBytes()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(member.getUsername()).append("님").append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("Pay1oad 회원 생성이 성공적으로 완료되었습니다.");

        buffer.append("이 링크를 따라서 회원가입을 완료해 주세요: http://localhost:8080/verify/email?id=").append(encodedVerificationId);
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("만약 이 메일이 온 이유를 모르겠다면 무시하셔도 좋습니다.");
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("감사합니다.").append(System.lineSeparator());
        return buffer.toString();
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
