package com.xiilab.data_learnway.test.exception;

import org.springframework.http.HttpStatus;

import com.xiilab.data_learnway.global.exception.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum TestErrorCode implements ErrorCode {
	TEST_ERROR(HttpStatus.BAD_REQUEST.value(), "테스트 에러!!!"),
	TEST_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Test를 찾지못했습니다."),
	UPDATE_TEST_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR.value(), "수정할 Test를 찾지 못했습니다."),
	UPDATE_TEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Test 수정을 실패했습니다."),
	DELETE_TEST_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR.value(), "삭제할 Test를 찾지 못했습니다."),
	;
	private final int code;
	private final String message;
}
