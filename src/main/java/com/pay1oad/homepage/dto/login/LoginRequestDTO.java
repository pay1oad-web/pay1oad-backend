package com.pay1oad.homepage.dto.login;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class LoginRequestDTO {

    @Getter
    public static class toSignInDTO{
        @NotNull
        @Pattern(regexp = "^.{1,20}$", message = "사용자 이름은 20자 이내로 입력해주세요.")
        private String userName;
        @NotNull
        private String passwd;
    }

    @Getter
    public static class toSignUpDTO{
        @NotNull
        private String userName;
        @NotNull
        private String passwd;
        @NotNull
        private String email;
    }
}
