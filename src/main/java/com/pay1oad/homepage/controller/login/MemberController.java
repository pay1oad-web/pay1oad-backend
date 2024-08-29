package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.dto.JwtToken;
import com.pay1oad.homepage.dto.login.LoginRequestDTO;
import com.pay1oad.homepage.dto.login.LoginResponseDTO;
import com.pay1oad.homepage.dto.login.MemberDTO;
import com.pay1oad.homepage.dto.ResponseDTO;
//import com.pay1oad.homepage.event.UserRegistrationEvent;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import com.pay1oad.homepage.response.code.status.InSuccess;
import com.pay1oad.homepage.security.JwtUtils;
import com.pay1oad.homepage.security.TokenProvider;
import com.pay1oad.homepage.service.login.JwtRedisService;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
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

    private final JwtUtils jwtUtils;

    private final MemberRepository memberRepository;


    /*private final JavaMailSender mailSender;
    private final EmailVerificationService verificationService;

    @Autowired
    public MemberController(JavaMailSender mailSender, EmailVerificationService verificationService) {
        this.mailSender = mailSender;
        this.verificationService = verificationService;
    }*/

    @Operation(summary = "회원가입 API", description = "username, email, passwd를 입력하세요. 나머지 DTO 형식은 무시해도 됩니다.")
    @PostMapping("/signup")
    public ResForm<LoginResponseDTO.toSignUpDTO> registerUser(@RequestBody LoginRequestDTO.toSignUpDTO signUpDTO) {
        LoginResponseDTO.toSignUpDTO responseSignUpDTO = memberService.signUp(signUpDTO);
        return ResForm.onSuccess(InSuccess.SIGNUP_SUCCESS, responseSignUpDTO);
    }

    @Operation(summary = "로그인 API", description = "username, passwd를 입력하세요. 나머지 DTO 형식은 무시해도 됩니다.")
    @PostMapping("/signin")
    public ResForm<LoginResponseDTO.toSignInDTO> authenticate(@Valid @RequestBody LoginRequestDTO.toSignInDTO signInDTO){
        LoginResponseDTO.toSignInDTO toSignInDTO = memberService.signIn(signInDTO);
        return ResForm.onSuccess(InSuccess.LOGIN_SUCCESS, toSignInDTO);
    }

    @Operation(summary = "로그아웃 API", description = "JWT 토큰으로 해당 유저를 로그아웃 합니다. 서버에서 기존 토큰을 무효화 합니다.")
    @PostMapping("/signout")
    public String signout(HttpServletRequest httpServletRequest){

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

                //logout -> redis에서 refresh token 제거 및 access token 블랙리스트로
                jwtRedisService.deleteValues(userid);

                String accessToken = jwtUtils.getToken(httpServletRequest);
                Long expiration = tokenProvider.getExpiration(accessToken);
                jwtRedisService.setBlackList(accessToken, "access_token", expiration);

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

    @Operation(summary = "토큰 유효화 API", description = "입력한 JWT 토큰을 유효화 합니다. 유효화 기간은 10분 입니다.")
    @PostMapping("/refreshToken")
    public JwtToken refresh(HttpServletRequest httpServletRequest){
        //get token
        String refreshToken = jwtUtils.getToken(httpServletRequest);
        //log.info("token: "+token);

        //get username
        int userid= Integer.parseInt(tokenProvider.validateAndGetUserId(refreshToken));
        String username=memberService.getUsername(userid);

        if (Objects.equals(jwtRedisService.getValues(username), refreshToken)) {
            log.info("Refreshed: "+username.replaceAll("[\r\n]",""));

            jwtRedisService.deleteValues(username);

            //member 객체
            Member member = memberService.getMemberByID(userid);

            final JwtToken token=tokenProvider.create(member);

            //refresh
            jwtRedisService.setValues(username, token.getRefreshToken(), Duration.ofDays(3));

            return token;


        }else{
            throw new CustomException(ErrorStatus.REFRESH_TOKEN_NOT_VALID);
        }

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

}
