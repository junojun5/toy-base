package com.xiilab.data_learnway.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	//    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), "잘못된 매개변수가 포함됐습니다."),
	//    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리소스를 찾을 수 없습니다."),
	//    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류 \\n새로고침 후 다시 시도해주시고 에러가 지속적으로 발생할 경우 관리자에게 문의해주시길 바랍니다."),
	//    ;
	//    private final MessageSource messageSource;
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), "global.exception.badRequest"),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "global.exception.notFound"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "global.exception.internalServerError");

	private final int code;
	private final String message;

}

