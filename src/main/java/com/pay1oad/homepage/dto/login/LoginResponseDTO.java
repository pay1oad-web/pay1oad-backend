package com.pay1oad.homepage.dto.login;

import lombok.Builder;
import lombok.Getter;

public class LoginResponseDTO {
    @Getter
    @Builder
    public static class toSignUpDTO{

        private String userName;
        private String accessToken;
        private String refreshToken;

    }
}
