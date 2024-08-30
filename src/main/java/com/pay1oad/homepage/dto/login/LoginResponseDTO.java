package com.pay1oad.homepage.dto.login;

import lombok.Builder;
import lombok.Getter;

public class LoginResponseDTO {
    @Getter
    @Builder
    public static class toSignInDTO {
        private String userName;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class toSignUpDTO{
        private String userName;
        private String email;
    }

    @Getter
    @Builder
    public static class toRefreshDTO {
        private String userName;
        private String accessToken;
        private String refreshToken;
    }
}
