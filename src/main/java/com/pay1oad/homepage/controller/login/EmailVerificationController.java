package com.pay1oad.homepage.controller.login;

//import com.pay1oad.homepage.event.UserRegistrationEvent;
//import com.pay1oad.homepage.listener.EmailVerificationListener;
import com.pay1oad.homepage.dto.login.MemberDTO;
import com.pay1oad.homepage.exception.CustomException;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.model.login.MemberAuth;
import com.pay1oad.homepage.persistence.login.MemberRepository;
import com.pay1oad.homepage.response.code.status.ErrorStatus;
import com.pay1oad.homepage.security.JwtUtils;
import com.pay1oad.homepage.service.email.EmailService;
import com.pay1oad.homepage.service.login.EmailVerificationService;
import com.pay1oad.homepage.service.login.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService verificationService;
    private final MemberService memberService;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;
    private final MemberRepository memberRepository;

    @GetMapping("/verify/sendmail")
    public ResponseEntity<?> sendVerificationEmail(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String username = jwtUtils.getAccountIdFromRequest(authorizationHeader);
            String verificationId = verificationService.generateVerification(username);
            Member member = memberRepository.findByUsername(username);
            if(!member.getMemberAuth().equals(MemberAuth.UNAUTH)){
                return ResponseEntity.ok().body("Already Authorized.");
            }
            emailService.sendVerificationEmail(username, member.getEmail(), verificationId);
            return ResponseEntity.ok().body("Email Succesfuly Send.");
        } catch (NoSuchAlgorithmException e) {
            log.error("Email send Failed.", e);
            throw new CustomException(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id) {
        try {
            byte[] decodedId = Base64.getDecoder().decode(id);
            String verificationId = new String(decodedId);
            String username = verificationService.getUsernameForVerificationId(verificationId);

            if (username != null) {
                Member member = memberService.checkID(username);
                if (member != null) {
                    member.setVerified(true);
                    if(member.getMemberAuth().equals(MemberAuth.UNAUTH)){
                        member.setMemberAuth(MemberAuth.MEMBER);
                        memberService.save(member);
                        return "Email Verification Success";
                    }else{
                        return "Email Verification Already Success";
                    }

                    //return "redirect:/success";
                }
            }
        } catch (Exception e) {
            log.error("이메일 인증 실패", e);
        }
        return "이메일 인증 실패.";
    }
}
