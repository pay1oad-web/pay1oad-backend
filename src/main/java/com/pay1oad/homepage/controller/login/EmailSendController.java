package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.response.ResForm;
import com.pay1oad.homepage.response.code.status.InSuccess;
import com.pay1oad.homepage.service.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailSendController {
    private final EmailService emailService;

    @Operation(summary = "회원가입 시 인증 이메일을 발송하는 API", description = "jwt토큰을 조회하여 해당 jwt 주인의 이메일로 인증 이메일이 발신됩니다.")
    @PostMapping("/verify/sendmail")
    public ResForm<String> sendVerificationEmail(HttpServletRequest httpServletRequest) {
        String emailSendResult = emailService.sendVerificationEmailService(httpServletRequest);
        return ResForm.onSuccess(InSuccess.EMAIL_SEND_SUCCESS , emailSendResult);
    }
}
