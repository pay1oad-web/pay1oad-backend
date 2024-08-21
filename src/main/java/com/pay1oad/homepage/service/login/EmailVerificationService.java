package com.pay1oad.homepage.service.login;

import com.pay1oad.homepage.model.login.EmailVerification;
import com.pay1oad.homepage.persistence.login.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailVerificationService {

    @Autowired
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailVerificationService(EmailVerificationRepository emailVerificationRepository){
        this.emailVerificationRepository = emailVerificationRepository;
    }

    public String generateVerification(String username){
        if(!emailVerificationRepository.existsById(username)){
            EmailVerification emailVerification=new EmailVerification();
            emailVerification.setUsername(username);
            emailVerification=emailVerificationRepository.save(emailVerification);
            return emailVerification.getVerificationID();
        }
        return null;
    }

    public String getUsernameForVerificationId(String verificationId){
        Optional<EmailVerification> verification=emailVerificationRepository.findById(verificationId);
        if(verification.isPresent()){
            return verification.get().getUsername();
        }
        return null;

    }
}
