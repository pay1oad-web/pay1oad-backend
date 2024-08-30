package com.pay1oad.homepage.controller.login;

import com.pay1oad.homepage.service.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailService emailService;

    @Operation(summary = "회원가입 시 이메일을 인증하는 API", description = "발송된 이메일에서 주어진 링크를 클릭하면 해당 api가 실행됩니다.")
    @GetMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestParam String id) {
        String emailVerificationResult = emailService.verifyEmail(id);

        return ResponseEntity.ok(emailVerificationResult);
    }
}
