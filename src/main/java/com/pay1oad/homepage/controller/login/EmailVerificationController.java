package com.pay1oad.homepage.controller.login;

//import com.pay1oad.homepage.event.UserRegistrationEvent;
//import com.pay1oad.homepage.listener.EmailVerificationListener;
import com.pay1oad.homepage.model.login.Member;
import com.pay1oad.homepage.service.login.EmailVerificationService;
import com.pay1oad.homepage.service.login.MemberService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

@Controller
public class EmailVerificationController {
    @Autowired
    private EmailVerificationService verificationService;
    @Autowired
    private MemberService memberService;


    //private static final Logger log = LoggerFactory.getLogger(EmailVerificationListener.class);


    /*@GetMapping("/verify/sendemail")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO){
        log.info("email on ApplicationEvent");

        String verificationId = verificationService.generateVerification(MemberDTO.username);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Pay1oad 계정 생성 확인해주세요");
        message.setText(getText(member, verificationId));
        message.setTo(email);
        mailSender.send(message);
    }*/

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



    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id) {
        byte[] actualId = Base64.getDecoder().decode(id.getBytes());
        String username = verificationService.getUsernameForVerificationId(new String(actualId));
        if(username != null) {
            Member member = memberService.checkID(username);
            member.setVerified(true);
            memberService.save(member);
            return "redirect:/";
        }
        return "redirect:/";
    }
}
