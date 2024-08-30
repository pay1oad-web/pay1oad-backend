package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.dto.login.LoginRequestDTO;
import com.pay1oad.homepage.dto.login.LoginResponseDTO;
import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.InSuccess;
import com.pay1oad.homepage.service.login.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.ApplicationEventPublisher;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

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
    public ResForm<String> signout(HttpServletRequest httpServletRequest){
        String signOutReturn = memberService.signOut(httpServletRequest);
        return ResForm.onSuccess(InSuccess.SIGNOUT_SUCCESS, signOutReturn);
    }

    @Operation(summary = "토큰 유효화 API", description = "입력한 JWT 토큰을 유효화 합니다. 유효화 기간은 10분 입니다.")
    @PostMapping("/refreshToken")
    public ResForm<LoginResponseDTO.toRefreshDTO> refresh(HttpServletRequest httpServletRequest){
        LoginResponseDTO.toRefreshDTO toRefreshDTO = memberService.refreshToken(httpServletRequest);
        return ResForm.onSuccess(InSuccess.TOKEN_REFRESH_SUCCESS, toRefreshDTO);
    }
}
