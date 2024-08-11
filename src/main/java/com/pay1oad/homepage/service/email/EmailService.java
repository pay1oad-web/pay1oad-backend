package com.pay1oad.homepage.service.email;

import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;



import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String text) throws NoSuchAlgorithmException {

        try {
            sendMail(to, text);
        } catch (MessagingException e) {
            // Log error and throw a custom exception or handle the exception
            log.error("메일 전송 실패! 유효하지 않은 이메일: {}", to, e);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }
    }

    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject("회원가입 이메일 인증");
        helper.setText(content, true);
        mailSender.send(mimeMessage);
        log.info("메일 전송 완료: {}", email);
    }

    private String createMailContent(String username, String verificationId) {
        String encodedVerificationId = Base64.getEncoder().encodeToString(verificationId.getBytes());
        return String.format(
                "<html>" +
                        "<body>" +
                        "<p>%s님,</p>" +
                        "<p>Pay1oad 회원 생성이 성공적으로 완료되었습니다.</p>" +
                        "<p>아래 링크를 클릭하여 회원가입을 완료해 주세요:</p>" +
                        "<p><a href='http://localhost:8080/verify/email?id=%s'>회원가입 완료하기</a></p>" +
                        "<p>만약 이 메일이 온 이유를 모르겠다면 무시하셔도 좋습니다.</p>" +
                        "<p>감사합니다.</p>" +
                        "</body>" +
                        "</html>",
                username, encodedVerificationId
        );
    }

    public void sendVerificationEmail(String username, String email, String verificationId) throws NoSuchAlgorithmException {
        String content = createMailContent(username, verificationId);
        sendVerificationEmail(email, content);
    }
}
