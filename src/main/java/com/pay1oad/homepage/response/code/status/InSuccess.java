package com.pay1oad.homepage.response.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum InSuccess {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    LOGIN_SUCCESS(HttpStatus.OK, "MEMBER2001", "로그인에 성공했습니다."),
    SIGNUP_SUCCESS(HttpStatus.OK, "MEMBER2002", "회원가입에 성공했습니다."),
    SIGNOUT_SUCCESS(HttpStatus.OK, "MEMBER2003", "로그아웃에 성공했습니다."),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "MEMBER2004", "토큰 재발급에 성공했습니다."),

    AUTH_CHANGED(HttpStatus.OK, "ADMIN2001", " 권한이 성공적으로 변경되었습니다."),

    EMAIL_SEND_SUCCESS(HttpStatus.OK, "EMAIL2001", "이메일이 성공적으로 전송되었습니다."),
    EMAIL_VERIFICATION_SUCCESS(HttpStatus.OK, "EMAIL2002", "이메일이 성공적으로 인증되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
