package com.pay1oad.homepage.response.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _EMPTY_FIELD(HttpStatus.NO_CONTENT, "COMMON404", "입력 값이 누락되었습니다."),

    _OPERATORAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH401", "OPERATOR 권한은 ADMIN 사용자의 권한을 부여 및 수정이 불가합니다"),

    Member_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "Member not exist. Login Failed."),
    LOGIN_FAILED_BY_PASSWD_OR_MEMBER_NOT_EXIST(HttpStatus.UNAUTHORIZED, "MEMBER4002", "유저 또는 비밀번호가 잘못되었습니다."),
    PASSWD_FORMAT_NOT_VALID(HttpStatus.BAD_REQUEST, "MEMBER4003", "비밀번호 형식이 잘못되었습니다."),
    EMAIL_FORMAT_NOT_VALID(HttpStatus.BAD_REQUEST, "MEMBER4004", "이메일 형식이 잘못되었습니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "MEMBER4005", "이미 있는 사용자 이름입니다."),

    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ENAIL4001", "이메일 전송에 실패했습니다."),

    REFRESH_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "TOKEN4001", "해당 refresh token은 더 이상 유효하지 않습니다."),
    
    
    ;
    //
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
