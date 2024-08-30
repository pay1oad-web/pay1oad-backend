package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.InSuccess;
import com.pay1oad.homepage.service.email.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailService emailService;

    @Operation(summary = "회원가입 시 인증 이메일을 발송하는 API", description = "jwt토큰을 조회하여 해당 jwt 주인의 이메일로 인증 이메일이 발신됩니다.")
    @PostMapping("/verify/sendmail")
    public ResForm<String> sendVerificationEmail(HttpServletRequest httpServletRequest) {
        String emailSendResult = emailService.sendVerificationEmailService(httpServletRequest);
        return ResForm.onSuccess(InSuccess.EMAIL_SEND_SUCCESS , emailSendResult);
    }

    @Operation(summary = "회원가입 시 이메일을 인증하는 API", description = "발송된 이메일에서 주어진 링크를 클릭하면 해당 api가 실행됩니다.")
    @GetMapping("/verify/email")
    public ResForm<String> verifyEmail(@RequestParam String id) {
        String emailVerificationResult = emailService.verifyEmail(id);
        return ResForm.onSuccess(InSuccess.EMAIL_VERIFICATION_SUCCESS , emailVerificationResult);
    }
}
