package com.xiilab.data_learnway.auth.exception;

import org.springframework.http.HttpStatus;

import com.xiilab.data_learnway.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	UNAUTHORIZED_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호가 일치하지 않습니다."),
	DUPLICATE_EMAIL_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 사용 중인 계정입니다.\n다른 계정을 입력해주세요."),
	LOGIN_FAIL_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "아이디나 비밀번호가 일치하지 않습니다."),
	PASSWORD_MISSMATCH_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호가 일치하지 않습니다."),
	;

	private final int code;
	private final String message;

}
