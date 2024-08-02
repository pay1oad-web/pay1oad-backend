package com.pay1oad.homepage.persistence.login;

import com.pay1oad.homepage.model.login.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {


}
